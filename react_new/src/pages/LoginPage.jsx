import React, { useMemo, useState } from 'react';
import { useHistory } from 'react-router-dom';
import {
    IonButton,
    IonContent,
    IonHeader,
    IonInput,
    IonItem,
    IonLabel,
    IonList,
    IonPage,
    IonTitle,
    IonToolbar,
    IonIcon,
    IonCheckbox,
    IonFooter
} from '@ionic/react';
import { eye, eyeOff } from 'ionicons/icons';
import { SUPPORT_PHONE, HARDCODED_INFO } from '../constants';
import { useAuth } from '../contexts/AuthContext';

export default function LoginPage() {
    const history = useHistory();
    const { login, remember, setRemember } = useAuth();

    const [loginId, setLoginId] = useState('');
    const [loginPw, setLoginPw] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const appVersion = useMemo(() => 'v2026.02', []);

    const handleLogin = () => {
        if (!loginId || !loginPw) {
            window.alert('아이디와 비밀번호를 입력하세요.');
            return;
        }
        // 실제 로그인 처리는 여기서 API 호출 등을 수행
        login({ id: loginId, name: '사용자' }); // 임시 로그인 처리
        history.replace('/main');
    };

    const handleJoin = () => {
        window.alert('회원가입 신청 화면은 추후 연결 예정입니다.');
    };

    const renderHardcodedInfo = () => (
        <div style={{ padding: '20px', fontSize: '12px', color: '#666', background: '#f9f9f9' }}>
            <div style={{ fontWeight: 'bold', marginBottom: '8px' }}>하드코딩 접속정보</div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '4px' }}>
                <div>UUIDString: {HARDCODED_INFO.uuid}</div>
                <div>HP_SEQ: {HARDCODED_INFO.areaSeq}</div>
                <div>상태: {HARDCODED_INFO.grantState}</div>
                <div>모델: {HARDCODED_INFO.model}</div>
                <div>핸드폰: {HARDCODED_INFO.mobileNumber}</div>
                <div>로그인ID: {HARDCODED_INFO.userId}</div>
                <div>서버: {HARDCODED_INFO.serverHost}</div>
                <div>DB명: {HARDCODED_INFO.dbName}</div>
                <div>DB계정: {HARDCODED_INFO.dbUser}</div>
                <div>DB비번: {HARDCODED_INFO.dbPass}</div>
                <div>포트: {HARDCODED_INFO.port}</div>
                <div>영업소코드: {HARDCODED_INFO.areaCode}</div>
                <div>영업소명: {HARDCODED_INFO.areaName}</div>
                <div>마지막영업소: {HARDCODED_INFO.lastAreaCode}</div>
                <div>사원코드: {HARDCODED_INFO.employeeCode}</div>
                <div>사원명: {HARDCODED_INFO.employeeName}</div>
                <div>지역번호: {HARDCODED_INFO.phoneAreaNumber}</div>
                <div>서명경로: {HARDCODED_INFO.signPath}</div>
                <div>라이선스: {HARDCODED_INFO.licenseDate}</div>
                <div>가입일: {HARDCODED_INFO.joinDate}</div>
                <div>최종로그인: {HARDCODED_INFO.lastLoginDate}</div>
                <div>권한: {HARDCODED_INFO.menuPermission}</div>
                <div>메모: {HARDCODED_INFO.remark}</div>
                <div>가스타입: {HARDCODED_INFO.gasType}</div>
                <div>유료여부: {HARDCODED_INFO.chargeYn}</div>
            </div>
        </div>
    );

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonTitle>가스 경영 2025</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonContent className="ion-padding">
                <div style={{ textAlign: 'center', margin: '40px 0' }}>
                    <img src="/img/222.png" alt="앱 로고" style={{ width: '120px' }} />
                </div>

                <IonList>
                    <IonItem>
                        <IonLabel position="stacked">아이디</IonLabel>
                        <IonInput
                            value={loginId}
                            placeholder="아이디를 입력하세요"
                            onIonChange={e => setLoginId(e.detail.value)}
                        />
                    </IonItem>
                    <IonItem>
                        <IonLabel position="stacked">비밀번호</IonLabel>
                        <IonInput
                            type={showPassword ? 'text' : 'password'}
                            value={loginPw}
                            placeholder="비밀번호를 입력하세요"
                            onIonChange={e => setLoginPw(e.detail.value)}
                        />
                        <IonIcon
                            slot="end"
                            icon={showPassword ? eyeOff : eye}
                            onClick={() => setShowPassword(!showPassword)}
                            style={{ marginTop: '20px' }}
                        />
                    </IonItem>
                    <IonItem lines="none">
                        <IonCheckbox slot="start" checked={remember} onIonChange={e => setRemember(e.detail.checked)} />
                        <IonLabel>로그인 정보 저장</IonLabel>
                    </IonItem>
                </IonList>

                <div style={{ marginTop: '20px' }}>
                    <IonButton expand="block" onClick={handleLogin}>로그인</IonButton>
                    <IonButton expand="block" fill="outline" onClick={handleJoin} style={{ marginTop: '10px' }}>가입신청</IonButton>
                </div>

                <div style={{ marginTop: '40px' }}>
                    {renderHardcodedInfo()}
                </div>

                <div style={{ textAlign: 'center', marginTop: '20px', color: '#999', fontSize: '12px' }}>
                    {appVersion}
                </div>
            </IonContent>
            <IonFooter>
                <IonToolbar>
                    <div style={{ textAlign: 'center', fontSize: '12px', color: '#999', padding: '10px' }}>
                        Copyright © 2026 조아테크 | 고객지원 <a href={`tel:${SUPPORT_PHONE}`}>{SUPPORT_PHONE}</a>
                    </div>
                </IonToolbar>
            </IonFooter>
        </IonPage>
    );
}
