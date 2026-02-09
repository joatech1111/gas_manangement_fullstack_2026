require('dotenv').config();
const admin = require('firebase-admin');
const fs = require('fs');
const path = require('path');
const QRCode = require('qrcode');
const { execSync } = require('child_process');
const readline = require('readline');
const { MongoClient } = require('mongodb');

// 스크립트 실행 위치 (scripts 디렉토리)
const SCRIPT_DIR = __dirname;
// 프로젝트 루트 (front_gas4management)
const PROJECT_ROOT = path.join(SCRIPT_DIR, '..');
// Firebase 서비스 계정 키 파일 경로 (scripts 디렉토리에 위치해야 함)
const SERVICE_ACCOUNT_PATH = path.join(SCRIPT_DIR, 'firebase-service-account.json');

// 베타 릴리즈 사이트 경로 (Workspace 루트 기준 상위 등)
// gas_management_2023_fullstack -> front_gas4management -> scripts
// 상위로 3번 올라가면 IdeaProjects
const BETA_RELEASE_SITE_DIR = path.join(PROJECT_ROOT, '..', '..', 'beta-release-site');
const RELEASE_QR_DIR = path.join(BETA_RELEASE_SITE_DIR, 'public', 'qr');

// MongoDB 설정 (릴리즈 이력 저장용)
// 기존 Tank 2025와 동일한 DB 사용 (컬렉션 공유)
const MONGODB_USER = process.env.MONGODB_USER || 'admin';
const MONGODB_PASSWORD = process.env.MONGODB_PASSWORD || 'yourpassword'; // 실제 비밀번호 필요
const MONGODB_DB_NAME = process.env.MONGODB_DB_NAME || 'tank2025';
const MONGODB_HOST = process.env.MONGODB_EXTERNAL_HOST || '183.111.26.205';
const MONGODB_PORT = process.env.MONGODB_EXTERNAL_PORT || '13004';
const RELEASE_HISTORY_COLLECTION = 'tank2025_release_history'; // 기존 컬렉션 사용

const MONGODB_URI = process.env.MONGODB_URI
    ? process.env.MONGODB_URI
    : `mongodb://${MONGODB_USER}:${MONGODB_PASSWORD}@${MONGODB_HOST}:${MONGODB_PORT}/${MONGODB_DB_NAME}?authSource=admin`;

// APK/IPA 경로 설정 (Capacitor 프로젝트 구조에 맞게 수정)
const APK_OUTPUT_DIR = path.join(PROJECT_ROOT, 'android', 'app', 'build', 'outputs', 'apk', 'release');
const APK_FILE_NAME = 'app-release.apk'; // 기본 생성 파일명
const APK_FILE_PATH = path.join(APK_OUTPUT_DIR, APK_FILE_NAME);

// iOS IPA 경로는 빌드 방식에 따라 다름 (수동 빌드 후 특정 위치에 있다고 가정하거나 찾기)
const IOS_OUTPUT_DIR = path.join(PROJECT_ROOT, 'ios', 'App', 'App', 'build'); // 예시 경로, 실제로는 다를 수 있음

// Firebase Storage 버킷 이름
const STORAGE_BUCKET = 'pika-jessica.appspot.com';

// Firebase 앱 인스턴스 캐시
let firebaseApp = null;
let storageBucket = null;

// Firebase 초기화 (중복 방지)
function initializeFirebase() {
    try {
        if (firebaseApp && storageBucket) {
            return storageBucket;
        }

        if (admin.apps.length > 0) {
            console.log('✅ 기존 Firebase 앱 사용');
            firebaseApp = admin.app();
        } else {
            if (!fs.existsSync(SERVICE_ACCOUNT_PATH)) {
                throw new Error(`Firebase 서비스 계정 키 파일을 찾을 수 없습니다: ${SERVICE_ACCOUNT_PATH}\nfirebase-service-account.json 파일을 scripts 디렉토리에 복사해주세요.`);
            }
            const serviceAccount = require(SERVICE_ACCOUNT_PATH);

            firebaseApp = admin.initializeApp({
                credential: admin.credential.cert(serviceAccount),
                storageBucket: STORAGE_BUCKET
            });

            console.log('✅ Firebase 초기화 완료');
        }

        storageBucket = admin.storage().bucket();
        return storageBucket;

    } catch (error) {
        console.error('❌ Firebase 초기화 실패:', error.message);
        throw error;
    }
}

// Firebase Storage에 파일 업로드
async function uploadToFirebaseStorage(filePath, fileName) {
    try {
        const bucket = initializeFirebase();

        if (!fs.existsSync(filePath)) {
            throw new Error(`파일을 찾을 수 없습니다: ${filePath}`);
        }

        const fileStats = fs.statSync(filePath);
        const fileSizeMB = (fileStats.size / (1024 * 1024)).toFixed(2);
        console.log(`📱 업로드할 파일: ${fileName} (${fileSizeMB}MB)`);

        const storageRef = `releases/${fileName}`;
        const file = bucket.file(storageRef);

        console.log('🚀 Firebase Storage 업로드 시작...');
        const startTime = Date.now();

        const ext = path.extname(fileName).toLowerCase();
        const contentType = ext === '.apk'
            ? 'application/vnd.android.package-archive'
            : ext === '.ipa'
                ? 'application/octet-stream'
                : 'application/octet-stream';

        await file.save(fs.readFileSync(filePath), {
            metadata: {
                contentType,
                metadata: {
                    uploadedAt: new Date().toISOString(),
                    originalName: path.basename(filePath),
                    fileSize: fileStats.size.toString()
                }
            }
        });

        const endTime = Date.now();
        const uploadTime = ((endTime - startTime) / 1000).toFixed(2);

        console.log('✅ Firebase Storage 업로드 완료!');
        console.log(`📱 파일명: ${fileName}`);
        console.log(`📁 Storage 경로: ${storageRef}`);
        console.log(`⏱️  업로드 시간: ${uploadTime}초`);

        await file.makePublic();
        const publicUrl = `https://storage.googleapis.com/${STORAGE_BUCKET}/${storageRef}`;
        console.log(`🔗 공개 URL: ${publicUrl}`);

        const [signedUrl] = await file.getSignedUrl({
            action: 'read',
            expires: Date.now() + 7 * 24 * 60 * 60 * 1000,
        });
        console.log(`🔒 서명된 URL (7일간 유효): ${signedUrl}`);

        return {
            fileName,
            storageRef,
            publicUrl,
            signedUrl,
            uploadTime
        };

    } catch (error) {
        console.error('❌ Firebase Storage 업로드 오류:', error.message);
        throw error;
    }
}

// 릴리즈 노트 읽기
function readAndDisplayReleaseNotes() {
    try {
        const releaseFilePath = path.join(SCRIPT_DIR, 'release.txt');
        if (fs.existsSync(releaseFilePath)) {
            const content = fs.readFileSync(releaseFilePath, 'utf8');

            console.log('\n' + '='.repeat(60));
            console.log('📋                  릴리즈 노트                    📋');
            console.log('='.repeat(60));

            const lines = content.trim().split('\n');
            lines.forEach(line => {
                if (line.trim()) {
                    if (line.includes('v') && line.includes('.')) {
                        console.log(`🚀 ${line}`);
                        console.log('-'.repeat(40));
                    } else if (line.includes('🎯') || line.includes('🐛') || line.includes('📱') || line.includes('⚠️')) {
                        console.log(`\n${line}`);
                    } else {
                        console.log(`   ${line}`);
                    }
                } else {
                    console.log('');
                }
            });

            console.log('='.repeat(60));
            return content.trim();
        } else {
            console.log('\n⚠️ release.txt 파일을 찾을 수 없습니다.');
            return null;
        }
    } catch (error) {
        console.error('❌ 릴리즈 노트 읽기 오류:', error.message);
        return null;
    }
}

// 릴리즈 노트 원문 읽기
function readReleaseNotesRaw() {
    try {
        const releaseFilePath = path.join(SCRIPT_DIR, 'release.txt');
        if (!fs.existsSync(releaseFilePath)) {
            return null;
        }
        return fs.readFileSync(releaseFilePath, 'utf8').trim();
    } catch (error) {
        console.error('❌ 릴리즈 노트 읽기 오류:', error.message);
        return null;
    }
}

// 릴리즈 노트 입력
async function promptReleaseNotesInput() {
    const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
    const ask = (question) => new Promise(resolve => rl.question(question, resolve));

    console.log('\n📝 릴리즈 노트를 입력해주세요.');
    console.log('입력 종료: 한 줄에 END 입력');
    console.log('예시 첫 줄: v1.0.0 - 기능 설명 (2025-02-09)');

    const lines = [];
    while (true) {
        const line = await ask('> ');
        if (line.trim().toLowerCase() === 'end') break;
        lines.push(line);
    }

    rl.close();

    const content = lines.join('\n').trim();
    if (!content) {
        console.log('⚠️ 릴리즈 노트 입력이 비어 있습니다. 기존 release.txt를 사용합니다.');
        return null;
    }

    try {
        const releaseFilePath = path.join(SCRIPT_DIR, 'release.txt');
        fs.writeFileSync(releaseFilePath, content, 'utf8');
        console.log(`✅ 릴리즈 노트 저장 완료: ${releaseFilePath}`);
        return content;
    } catch (error) {
        console.error('❌ 릴리즈 노트 저장 실패:', error.message);
        return null;
    }
}

// 콘솔 노출 여부
async function promptReleaseVisibility() {
    const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
    const ask = (question) => new Promise(resolve => rl.question(question, resolve));
    const answer = await ask('콘솔 UI에 노출할까요? (y/n, 기본 y): ');
    rl.close();
    const normalized = (answer || '').trim().toLowerCase();
    return !(normalized === 'n' || normalized === 'no');
}

// 첫 줄에서 버전 추출
function parseVersionTitle(releaseNotes) {
    if (!releaseNotes) return null;
    const firstLine = releaseNotes.split('\n')[0].trim();
    return firstLine || null;
}

// 디렉토리 보장
function ensureDir(dirPath) {
    if (!fs.existsSync(dirPath)) {
        fs.mkdirSync(dirPath, { recursive: true });
    }
}

// QR 복사
function copyQrToSite(qrFilePath, qrFileName) {
    try {
        // 베타 사이트 디렉토리 존재 여부 확인
        if (!fs.existsSync(RELEASE_QR_DIR)) {
            console.log(`⚠️ 베타 릴리즈 사이트 QR 디렉토리(${RELEASE_QR_DIR})가 존재하지 않아 생략합니다.`);
            // 로컬에라도 저장? 이미 생성됨.
            return null;
        }
        ensureDir(RELEASE_QR_DIR);
        const destPath = path.join(RELEASE_QR_DIR, qrFileName);
        fs.copyFileSync(qrFilePath, destPath);
        return `/qr/${qrFileName}`;
    } catch (error) {
        console.error('⚠️ QR 이미지 복사 실패:', error.message);
        return null;
    }
}

// 이미지 base64
function readImageAsBase64(filePath) {
    try {
        if (!fs.existsSync(filePath)) return null;
        return fs.readFileSync(filePath).toString('base64');
    } catch (error) {
        console.error('❌ 이미지 base64 변환 실패:', error.message);
        return null;
    }
}

// 릴리즈 이력 저장
async function insertReleaseHistoryToMongo(entry) {
    let client = null;
    try {
        const doc = {
            id: `${entry.platform}-${entry.fileName}`,
            platform: entry.platform,
            fileName: entry.fileName,
            fileSizeMB: entry.fileSizeMB,
            publicUrl: entry.publicUrl,
            signedUrl: entry.signedUrl,
            uploadedAt: entry.uploadedAt,
            versionTitle: entry.versionTitle,
            releaseNotes: entry.releaseNotes,
            qrImagePath: entry.qrImagePath,
            qrImageBase64: entry.qrImageBase64,
            qrImageMimeType: entry.qrImageMimeType,
            visible: entry.visible !== false,
            createdAt: new Date(),
            appName: 'Gas Management 2025' // 기존 Tank 앱과 구분하기 위해 추가 (스키마가 허용한다면)
        };

        client = new MongoClient(MONGODB_URI, { serverSelectionTimeoutMS: 10000 });
        await client.connect();
        const db = client.db(MONGODB_DB_NAME);
        const collection = db.collection(RELEASE_HISTORY_COLLECTION);
        await collection.insertOne(doc);

        console.log('✅ MongoDB 릴리즈 이력 저장 완료');
    } catch (error) {
        console.error('❌ MongoDB 릴리즈 이력 저장 실패:', error.message);
    } finally {
        if (client) {
            await client.close();
        }
    }
}

// QR 생성
async function generateQRCodeOnly(url, outputFileName = 'qr-code.png') {
    try {
        const qrFilePath = path.join(SCRIPT_DIR, outputFileName);
        console.log('📱 고품질 QR 코드 생성 중...');

        await QRCode.toFile(qrFilePath, url, {
            width: 1200,
            margin: 6,
            errorCorrectionLevel: 'H',
            type: 'png',
            quality: 1.0,
            color: {
                dark: '#000000',
                light: '#FFFFFF'
            }
        });

        console.log(`✅ 고품질 QR 코드 생성 완료: ${qrFilePath}`);

        // 자동 열기
        const { exec } = require('child_process');
        const os = require('os');
        let openCommand;
        switch (os.platform()) {
            case 'darwin': openCommand = `open "${qrFilePath}"`; break;
            case 'win32': openCommand = `start "" "${qrFilePath}"`; break;
            case 'linux': openCommand = `xdg-open "${qrFilePath}"`; break;
        }
        if (openCommand) exec(openCommand);

        return {
            fileName: outputFileName,
            filePath: qrFilePath,
            size: '1200x1200px'
        };
    } catch (error) {
        console.error('❌ QR 코드 생성 실패:', error.message);
        throw error;
    }
}

// 파일 목록
async function listFiles() {
    try {
        const bucket = initializeFirebase();
        const [files] = await bucket.getFiles({ prefix: 'releases/' });
        console.log('\n📁 업로드된 파일 목록:');
        files.forEach((file, index) => {
            console.log(`${index + 1}. ${file.name}`);
        });
        return files;
    } catch (error) {
        console.error('❌ 파일 목록 조회 오류:', error.message);
        throw error;
    }
}

// 정리
function cleanupFirebase() {
    try {
        if (firebaseApp) {
            firebaseApp = null;
            storageBucket = null;
        }
    } catch (error) { console.error('⚠️ Firebase 정리 오류:', error.message); }
}
process.on('exit', cleanupFirebase);
process.on('SIGINT', () => { cleanupFirebase(); process.exit(0); });

// 메인 함수
async function main() {
    try {
        console.log('🚀 Gas Management 2025 릴리즈 빌드 및 업로드 시작...\n');

        const target = process.env.TARGET || (process.argv[2] || 'android'); // android | ios

        await promptReleaseNotesInput();
        const isVisible = await promptReleaseVisibility();

        // 1) 플랫폼별 빌드
        if (target === 'android') {
            try {
                console.log('🚧 Capacitor Android 빌드 시작...');

                // Vite 빌드 (웹 에셋)
                console.log('Running: npm run build');
                execSync('npm run build', { cwd: PROJECT_ROOT, stdio: 'inherit' });

                // Capacitor Sync
                console.log('Running: npx cap sync android');
                execSync('npx cap sync android', { cwd: PROJECT_ROOT, stdio: 'inherit' });

                // Gradle Build (Release)
                console.log('Running: ./gradlew assembleRelease');
                const gradlew = process.platform === 'win32' ? 'gradlew.bat' : './gradlew';
                execSync(`${gradlew} assembleRelease`, { cwd: path.join(PROJECT_ROOT, 'android'), stdio: 'inherit' });

                if (!fs.existsSync(APK_FILE_PATH)) {
                    // 혹시 다른 경로에 생성되었는지 확인
                    // app-release-unsigned.apk 등
                    const unsignedPath = path.join(APK_OUTPUT_DIR, 'app-release-unsigned.apk');
                    if (fs.existsSync(unsignedPath)) {
                        console.log('⚠️ 서명되지 않은 APK가 발견되었습니다. (app-release-unsigned.apk) 이를 사용합니다.');
                        fs.copyFileSync(unsignedPath, APK_FILE_PATH);
                    } else {
                        throw new Error(`APK 산출물을 찾을 수 없습니다: ${APK_FILE_PATH}`);
                    }
                }

                const size = (fs.statSync(APK_FILE_PATH).size / (1024 * 1024)).toFixed(2);
                console.log(`✅ 안드로이드 빌드 완료: ${APK_FILE_PATH} (${size}MB)`);

            } catch (buildError) {
                console.error('❌ Android 빌드 실패:', buildError.message);
                throw buildError;
            }
        } else if (target === 'ios') {
            console.log('🍎 iOS 빌드는 현재 자동화되어 있지 않습니다.');
            console.log('Xcode에서 Archive 후 Export된 IPA 파일이 준비되어 있어야 합니다.');
            // 여기서 IPA 찾기 로직 추가 가능 (예: 다운로드 폴더나 특정 export 폴더)
            console.log('⚠️ iOS 빌드 구현 필요');
            return;
        } else {
            throw new Error(`알 수 없는 TARGET: ${target}`);
        }

        // 2) 날짜 포함 파일명 생성
        const currentDate = new Date().toISOString().slice(0, 10);
        const timestamp = new Date().toTimeString().slice(0, 8).replace(/:/g, '-');
        const fileName = `gas-management-release-${currentDate}-${timestamp}.${target === 'ios' ? 'ipa' : 'apk'}`;

        // 3) 업로드
        const result = await uploadToFirebaseStorage(APK_FILE_PATH, fileName);
        const fileSizeMB = (fs.statSync(APK_FILE_PATH).size / (1024 * 1024)).toFixed(2);

        // 4) 목록 조회
        await listFiles();

        console.log('\n✨ 업로드 성공!');

        // 5) QR 코드 생성
        const highQualityQR = await generateQRCodeOnly(
            result.publicUrl,
            `qr-${fileName.replace(/\.(apk|ipa)$/i, '.png')}`
        );
        console.log(`🖼️ QR 코드: ${highQualityQR.fileName}`);

        // 6) 릴리즈 이력 저장
        const releaseNotes = readReleaseNotesRaw();
        const versionTitle = parseVersionTitle(releaseNotes);
        const qrImagePath = copyQrToSite(highQualityQR.filePath, highQualityQR.fileName);
        const qrImageBase64 = readImageAsBase64(highQualityQR.filePath);

        await insertReleaseHistoryToMongo({
            platform: target,
            fileName,
            fileSizeMB,
            publicUrl: result.publicUrl,
            signedUrl: result.signedUrl,
            uploadedAt: new Date().toISOString(),
            versionTitle,
            releaseNotes,
            qrImagePath, // 베타 사이트가 존재하면 경로 저장
            qrImageBase64,
            qrImageMimeType: 'image/png',
            visible: isVisible
        });

    } catch (error) {
        console.error('💥 실행 실패:', error.message);
        cleanupFirebase();
        process.exit(1);
    }
}

// 실행
const args = process.argv.slice(2);
if (args.length > 0 && (args[0] === 'list' || args[0] === 'delete')) {
    // list, delete 구현 가능 (생략)
    main();
} else {
    main();
}
