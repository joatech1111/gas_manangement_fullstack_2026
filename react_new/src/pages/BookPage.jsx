import React, { useMemo, useState, useEffect } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import {
    IonPage,
    IonHeader,
    IonToolbar,
    IonButtons,
    IonButton,
    IonTitle,
    IonContent,
    IonList,
    IonItem,
    IonLabel,
    IonIcon,
    IonDatetime,
    IonDatetimeButton,
    IonModal,
    IonCard,
    IonCardContent,
    IonBadge,
    IonText
} from '@ionic/react';
import { arrowBack } from 'ionicons/icons';
import { fetchCustomerBookVolumeReadMeter, fetchCustomerBookWeight } from '../api/gasmax';

// Helper functions
function normalizeItems(result) {
    if (Array.isArray(result?.items)) return result.items;
    if (result?.items && typeof result.items === 'object') return Object.values(result.items);
    return [];
}

function formatDateParam(date) {
    if (!date) return '';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}${month}${day}`;
}

function subtractDays(date, days) {
    const copy = new Date(date);
    copy.setDate(copy.getDate() - days);
    return copy;
}

function isVolumeCustomer(customer) {
    const code = String(customer?.customerType || '').trim();
    if (code === '1' || code === '6') return true;
    const name = String(customer?.customerTypeName || '').trim();
    return name.includes('체적');
}

export default function BookPage() {
    const history = useHistory();
    const location = useLocation();
    const customer = location.state?.customer;

    const [bookMode, setBookMode] = useState('weight');
    const [bookStartDate, setBookStartDate] = useState(() => subtractDays(new Date(), 30).toISOString());
    const [bookEndDate, setBookEndDate] = useState(() => new Date().toISOString());
    const [bookRecords, setBookRecords] = useState([]);
    const [bookSummary, setBookSummary] = useState({ totalRowCount: 0, carriedOverAmount: '0', totalNowMonthAmount: '0' });
    const [bookError, setBookError] = useState('');
    const [loading, setLoading] = useState(false);

    const loadBook = async () => {
        if (!customer) return;
        setLoading(true);
        setBookError('');
        try {
            const start = new Date(bookStartDate);
            const end = new Date(bookEndDate);
            const startParam = formatDateParam(start);
            const endParam = formatDateParam(end);
            const isVolume = isVolumeCustomer(customer);

            const result = isVolume
                ? await fetchCustomerBookVolumeReadMeter(customer.customerCode, startParam, endParam)
                : await fetchCustomerBookWeight(customer.customerCode, startParam, endParam);

            if (result.session === 'X') {
                setBookError('세션이 만료되었습니다. 다시 로그인해주세요.');
                return;
            }
            if (result.error) {
                setBookError('거래장부 조회 중 오류가 발생했습니다.');
                return;
            }
            const items = normalizeItems(result);
            setBookRecords(items);
            setBookSummary({
                totalRowCount: result.totalRowCount || items.length,
                carriedOverAmount: result.carriedOverAmount || '0',
                totalNowMonthAmount: result.totalNowMonthAmount || '0',
            });
            setBookMode(isVolume ? 'volume' : 'weight');
        } catch (error) {
            console.error(error);
            setBookError('조회 중 오류가 발생했습니다.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (customer) {
            loadBook();
        }
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    if (!customer) {
        return (
            <IonPage>
                <IonHeader><IonToolbar><IonTitle>오류</IonTitle></IonToolbar></IonHeader>
                <IonContent className="ion-padding">
                    <p>거래처 정보가 없습니다.</p>
                    <IonButton onClick={() => history.goBack()}>뒤로가기</IonButton>
                </IonContent>
            </IonPage>
        );
    }

    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonButton onClick={() => history.goBack()}>
                            <IonIcon slot="icon-only" icon={arrowBack} />
                        </IonButton>
                    </IonButtons>
                    <IonTitle>거래장부</IonTitle>
                    <IonButtons slot="end">
                        <IonBadge color={bookMode === 'volume' ? 'tertiary' : 'warning'}>
                            {bookMode === 'volume' ? '체적' : '일반'}
                        </IonBadge>
                    </IonButtons>
                </IonToolbar>
            </IonHeader>

            <IonContent className="ion-padding">
                <IonCard>
                    <IonCardContent>
                        <h2>{customer.customerName}</h2>
                        <p>{customer.customerCode}</p>

                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '10px' }}>
                            <IonDatetimeButton datetime="startDate"></IonDatetimeButton>
                            <span>~</span>
                            <IonDatetimeButton datetime="endDate"></IonDatetimeButton>
                        </div>

                        <IonButton expand="block" style={{ marginTop: '10px' }} onClick={loadBook}>
                            조회
                        </IonButton>
                    </IonCardContent>
                </IonCard>

                <IonModal keepContentsMounted={true}>
                    <IonDatetime id="startDate" presentation="date" value={bookStartDate} onIonChange={e => setBookStartDate(e.detail.value)}></IonDatetime>
                </IonModal>
                <IonModal keepContentsMounted={true}>
                    <IonDatetime id="endDate" presentation="date" value={bookEndDate} onIonChange={e => setBookEndDate(e.detail.value)}></IonDatetime>
                </IonModal>

                <div style={{ padding: '0 10px', marginBottom: '10px', fontSize: '14px' }}>
                    <span>총 건수: <strong>{bookSummary.totalRowCount}</strong></span>
                    <span style={{ float: 'right' }}>
                        {bookMode === 'volume' ? `당월금액: ${bookSummary.totalNowMonthAmount}` : `이월잔액: ${bookSummary.carriedOverAmount}`}
                    </span>
                </div>

                {bookError && <p style={{ color: 'red', textAlign: 'center' }}>{bookError}</p>}

                <IonList>
                    {bookRecords.map((row, idx) => (
                        <IonItem key={`${row.collectDate || row.readMeterDate}-${row.sequenceNumber}-${idx}`} lines="full">
                            <div style={{ width: '100%', padding: '10px 0' }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', color: '#666' }}>
                                    <span>{row.readMeterDate || row.collectDate}</span>
                                    <IonBadge color="light">{bookMode === 'volume' ? '검침' : row.typeCode}</IonBadge>
                                </div>

                                {bookMode === 'volume' ? (
                                    <>
                                        <div style={{ fontWeight: 'bold', margin: '4px 0' }}>
                                            당검 {row.nowMonthReadMeter} / 사용 {row.useQuantity}
                                        </div>
                                        <div style={{ fontSize: '13px' }}>
                                            단가 {row.price} | 금액 {row.nowMonthAmount} | 수납 {row.collectDate || '미수'}
                                        </div>
                                        <div style={{ fontSize: '13px', color: '#888' }}>
                                            미납 {row.unpaidAmount} | 비고 {row.remark}
                                        </div>
                                    </>
                                ) : (
                                    <>
                                        <div style={{ fontWeight: 'bold', margin: '4px 0' }}>
                                            {row.itemName}
                                        </div>
                                        <div style={{ fontSize: '13px' }}>
                                            공급 {row.amount} | 입금 {row.collectAmount} | 미수 {row.unpaidAmount}
                                        </div>
                                        <div style={{ fontSize: '13px', color: '#888' }}>
                                            합계 {row.totalAmount} | 잔액 {row.remainAmount}
                                        </div>
                                    </>
                                )}
                            </div>
                        </IonItem>
                    ))}
                </IonList>

                {bookRecords.length === 0 && !loading && !bookError && (
                    <div style={{ textAlign: 'center', padding: '20px', color: '#999' }}>
                        거래장부 내역이 없습니다.
                    </div>
                )}

            </IonContent>
        </IonPage>
    );
}
