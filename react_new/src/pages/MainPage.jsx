

import React, { useState, useRef, useCallback } from 'react';
import { useHistory } from 'react-router-dom';
import {
    IonPage,
    IonHeader,
    IonToolbar,
    IonButtons,
    IonButton,
    IonTitle,
    IonContent,
    IonSearchbar,
    IonList,
    IonItem,
    IonLabel,
    IonIcon,
    IonLoading,
    IonModal,
    IonFooter,
    IonGrid,
    IonRow,
    IonCol
} from '@ionic/react';
import {
    logOutOutline,
    settingsOutline,
    searchOutline,
    peopleOutline,
    callOutline,
    documentTextOutline,
    walletOutline,
    createOutline,
    constructOutline
} from 'ionicons/icons';
import { searchCustomers, searchCustomersPage, fetchCustomerChoice } from '../api/gasmax';
import { BIZ_MENU } from '../constants';

// Mapping for Main Menu Icons
const MENU_ICONS = {
    cid: peopleOutline,
    sale: callOutline,
    unpaid: documentTextOutline,
    collect: walletOutline,
    meter: createOutline,
};

const MENU_LABELS = {
    cid: '거래처 관리',
    sale: '고객센터',
    unpaid: '미수금',
    collect: '수금 관리',
    meter: '검침 등록',
};

const MAIN_MENU_ITEMS = [
    { id: 'cid', label: '거래처 관리', icon: peopleOutline },
    { id: 'sale', label: '고객센터', icon: callOutline },
    { id: 'unpaid', label: '미수금', icon: documentTextOutline },
    { id: 'collect', label: '수금 관리', icon: walletOutline },
    { id: 'meter', label: '검침 등록', icon: createOutline },
];

export default function MainPage() {
    const history = useHistory();
    const [keyword, setKeyword] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [searchLoading, setSearchLoading] = useState(false);
    const [searchError, setSearchError] = useState('');
    const [nextPage, setNextPage] = useState(2);
    const [hasMore, setHasMore] = useState(false);

    const [selectedCustomer, setSelectedCustomer] = useState(null);
    const [detailModalOpen, setDetailModalOpen] = useState(false);

    // For Biz Menu
    const [bizMenuOpen, setBizMenuOpen] = useState(false);
    const [bizMenuCustomer, setBizMenuCustomer] = useState(null);

    // Long Press Logic
    const longPressTimer = useRef(null);
    const isLongPress = useRef(false);

    const startPress = useCallback((customer) => {
        isLongPress.current = false;
        longPressTimer.current = setTimeout(() => {
            isLongPress.current = true;
            openBizMenu(customer);
        }, 600); // 600ms threshold
    }, []);

    const cancelPress = useCallback(() => {
        if (longPressTimer.current) {
            clearTimeout(longPressTimer.current);
            longPressTimer.current = null;
        }
    }, []);

    const handleItemClick = (customer) => {
        if (isLongPress.current) {
            // If it was a long press, ignore the click (handled by timer)
            return;
        }
        cancelPress();
        handleCustomerClick(customer);
    };

    const handleSearch = async () => {
        if (!keyword.trim()) {
            setSearchError('검색어를 입력해주세요.');
            setSearchResults([]);
            return;
        }

        setSearchLoading(true);
        setSearchError('');
        setSelectedCustomer(null);

        try {
            const result = await searchCustomers(keyword.trim());
            if (result.session === 'X') {
                setSearchError('세션이 만료되었습니다. 다시 로그인해주세요.');
                setSearchResults([]);
                return;
            }
            if (result.error) {
                setSearchError('서버 응답 처리 중 오류가 발생했습니다.');
                setSearchResults([]);
                return;
            }

            let items = [];
            if (Array.isArray(result?.items)) items = result.items;
            else if (result?.items && typeof result.items === 'object') items = Object.values(result.items);

            setSearchResults(items);
            setNextPage(2);
            setHasMore(Boolean(result.hasMore));

            if (items.length === 0) {
                setSearchError('검색된 자료가 없습니다.');
            }
        } catch (error) {
            console.error(error);
            setSearchError('검색 중 오류가 발생했습니다.');
        } finally {
            setSearchLoading(false);
        }
    };

    const handleMore = async () => {
        setSearchLoading(true);
        try {
            const result = await searchCustomersPage(nextPage);
            if (result.session === 'X') {
                setSearchError('세션이 만료되었습니다. 다시 로그인해주세요.');
                setHasMore(false);
                return;
            }
            if (result.error) {
                setSearchError('서버 응답 처리 중 오류가 발생했습니다.');
                setHasMore(false);
                return;
            }

            let items = [];
            if (Array.isArray(result?.items)) items = result.items;
            else if (result?.items && typeof result.items === 'object') items = Object.values(result.items);

            setSearchResults((prev) => [...prev, ...items]);
            setNextPage((prev) => prev + 1);
            setHasMore(Boolean(result.hasMore));
        } catch (error) {
            console.error(error);
            setSearchError('검색 중 오류가 발생했습니다.');
        } finally {
            setSearchLoading(false);
        }
    };

    const handleMenu = (item) => {
        window.alert(`${item.label} 이동은 추후 연결 예정입니다.`);
    };

    const handleCustomerClick = async (customer) => {
        setSearchLoading(true);
        setSearchError('');
        try {
            const result = await fetchCustomerChoice(customer.areaCode, customer.customerCode);
            if (result.session === 'X') {
                setSearchError('세션이 만료되었습니다. 다시 로그인해주세요.');
                return;
            }
            if (result.error) {
                setSearchError('거래처 상세 조회 중 오류가 발생했습니다.');
                return;
            }
            setSelectedCustomer(result.item || customer);
            setDetailModalOpen(true);
        } catch (error) {
            console.error(error);
            setSearchError('거래처 상세 조회 중 오류가 발생했습니다.');
        } finally {
            setSearchLoading(false);
        }
    };

    const openBizMenu = (customer) => {
        setBizMenuCustomer(customer);
        setBizMenuOpen(true);
    };

    const handleBizMenuClick = (menu) => {
        setBizMenuOpen(false);
        if (!bizMenuCustomer) return;

        if (menu.id === 'detail') {
            handleCustomerClick(bizMenuCustomer);
            return;
        }
        if (menu.id === 'book') {
            history.push('/book', { customer: bizMenuCustomer });
            return;
        }
        window.alert(`${bizMenuCustomer.customerName} - ${menu.label} 화면은 추후 연결 예정입니다.`);
    };

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonButton onClick={() => history.push('/login')}>
                            <IonIcon slot="icon-only" icon={logOutOutline} />
                            로그아웃
                        </IonButton>
                    </IonButtons>
                    <IonTitle>가스 경영 2025</IonTitle>
                    <IonButtons slot="end">
                        <IonButton onClick={() => window.alert('설정 화면은 추후 연결 예정입니다.')}>
                            <IonIcon slot="icon-only" icon={settingsOutline} />
                            설정
                        </IonButton>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>

            <IonContent className="ion-padding">
                <div style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
                    <IonSearchbar
                        value={keyword}
                        onIonInput={e => setKeyword(e.detail.value)}
                        placeholder="거래처 검색"
                        onKeyDown={e => {
                            if (e.key === 'Enter') handleSearch();
                        }}
                    />
                    <IonButton onClick={handleSearch}>
                        <IonIcon slot="icon-only" icon={searchOutline} />
                    </IonButton>
                </div>

                {searchError && (
                    <div style={{ color: 'var(--ion-color-danger)', padding: '10px', textAlign: 'center' }}>
                        {searchError}
                    </div>
                )}

                <IonList>
                    {searchResults.map((item) => (
                        <div
                            key={`${item.areaCode}-${item.customerCode}`}
                            className="ion-activatable ripple-parent"
                            style={{
                                position: 'relative',
                                overflow: 'hidden',
                                borderBottom: '1px solid #ddd',
                                padding: '12px',
                                background: 'white'
                            }}
                            onMouseDown={() => startPress(item)}
                            onMouseUp={() => handleItemClick(item)}
                            onMouseLeave={cancelPress}
                            onTouchStart={() => startPress(item)}
                            onTouchEnd={() => handleItemClick(item)}
                            onTouchMove={cancelPress} // 스크롤 시 롱프레스 취소
                            onContextMenu={(e) => {
                                e.preventDefault();
                                // Native context menu prevent, handled by long press
                            }}
                        >
                            <div style={{ pointerEvents: 'none' }}> {/* Allow clicks to propagate to container */}
                                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '4px' }}>
                                    <img
                                        src={`/images/lbl_customer_type_${item.customerType}.png`}
                                        alt="구분"
                                        style={{ height: '20px', marginRight: '8px' }}
                                    />
                                    <span style={{
                                        fontWeight: 'bold',
                                        fontSize: '16px',
                                        color: item.customerStatusCode === '0' ? 'inherit' : '#999'
                                    }}>
                                        {item.customerName}
                                    </span>
                                    {item.customerStatusName && (
                                        <span style={{
                                            marginLeft: 'auto',
                                            fontSize: '12px',
                                            backgroundColor: '#eee',
                                            padding: '2px 6px',
                                            borderRadius: '4px'
                                        }}>
                                            {item.customerStatusName}
                                        </span>
                                    )}
                                </div>
                                <div style={{ fontSize: '14px', color: '#666' }}>
                                    {item.phoneNumber} {item.mobileNumber}
                                </div>
                                <div style={{ fontSize: '13px', color: '#888' }}>
                                    {item.address1} {item.address2}
                                </div>
                            </div>
                        </div>
                    ))}
                </IonList>

                {hasMore && (
                    <IonButton expand="block" fill="clear" onClick={handleMore}>
                        검색결과 더보기
                    </IonButton>
                )}

                {/* 메인 메뉴 그리드 */}
                <IonGrid style={{ marginTop: '20px' }}>
                    <IonRow>
                        {MAIN_MENU_ITEMS.map((item) => (
                            <IonCol size="4" key={item.id} style={{ textAlign: 'center' }}>
                                <div
                                    onClick={() => handleMenu(item)}
                                    className="ion-activatable ripple-parent"
                                    style={{ padding: '10px', borderRadius: '8px', position: 'relative', overflow: 'hidden' }}
                                >
                                    <IonIcon icon={item.icon} style={{ fontSize: '32px', color: '#3880ff' }} />
                                    <div style={{ fontSize: '12px', marginTop: '4px', color: '#333' }}>{item.label}</div>
                                </div>
                            </IonCol>
                        ))}
                    </IonRow>
                </IonGrid>

                <IonLoading isOpen={searchLoading} message="거래처 조회 중..." />

                {/* 거래처 상세 모달 */}
                <IonModal isOpen={detailModalOpen} onDidDismiss={() => setDetailModalOpen(false)}>
                    <IonHeader>
                        <IonToolbar>
                            <IonTitle>{selectedCustomer?.customerName}</IonTitle>
                            <IonButtons slot="end">
                                <IonButton onClick={() => setDetailModalOpen(false)}>닫기</IonButton>
                            </IonButtons>
                        </IonToolbar>
                    </IonHeader>
                    <IonContent className="ion-padding">
                        {selectedCustomer && (
                            <IonList>
                                <IonItem>
                                    <IonLabel position="stacked">거래처 코드</IonLabel>
                                    <p>{selectedCustomer.customerCode}</p>
                                </IonItem>
                                <IonItem>
                                    <IonLabel position="stacked">주소</IonLabel>
                                    <p>{selectedCustomer.address1} {selectedCustomer.address2}</p>
                                </IonItem>
                                <IonItem>
                                    <IonLabel position="stacked">전화</IonLabel>
                                    <p>{selectedCustomer.phoneNumber}</p>
                                </IonItem>
                                <IonItem>
                                    <IonLabel position="stacked">휴대폰</IonLabel>
                                    <p>{selectedCustomer.mobileNumber}</p>
                                </IonItem>
                            </IonList>
                        )}
                        <div className="ion-padding">
                            <IonButton expand="block" onClick={() => setDetailModalOpen(false)}>
                                확인
                            </IonButton>
                        </div>
                    </IonContent>
                </IonModal>

                {/* 업무 메뉴 모달 (Long Press 대용) */}
                <IonModal
                    isOpen={bizMenuOpen}
                    onDidDismiss={() => setBizMenuOpen(false)}
                    initialBreakpoint={0.5}
                    breakpoints={[0, 0.5]}
                >
                    <IonHeader>
                        <IonToolbar>
                            <IonTitle>{bizMenuCustomer?.customerName} 업무</IonTitle>
                        </IonToolbar>
                    </IonHeader>
                    <IonContent>
                        <IonList>
                            {BIZ_MENU.map((menu) => (
                                <IonItem button key={menu.id} onClick={() => handleBizMenuClick(menu)}>
                                    <IonLabel>{menu.label}</IonLabel>
                                </IonItem>
                            ))}
                        </IonList>
                    </IonContent>
                </IonModal>

            </IonContent>
            <IonFooter>
                <IonToolbar>
                    <div style={{ textAlign: 'center', fontSize: '12px', color: '#999', padding: '10px' }}>
                        Copyright © 2026 조아테크 | 고객지원 <a href="tel:1566-2399">1566-2399</a>
                    </div>
                </IonToolbar>
            </IonFooter>
        </IonPage>
    );
}
