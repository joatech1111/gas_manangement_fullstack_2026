import React, { useMemo, useRef, useState } from 'react';
import Calendar from 'react-calendar';
import {
  fetchCustomerBookVolumeReadMeter,
  fetchCustomerBookWeight,
  fetchCustomerChoice,
  searchCustomers,
  searchCustomersPage,
} from './api/gasmax.js';
import 'react-calendar/dist/Calendar.css';

const SUPPORT_PHONE = '1566-2399';
const HARDCODED_INFO = {
  uuid: 'b28618772c95b3a5',
  areaSeq: '5',
  grantState: 'Y',
  model: '하명현사장_tes',
  mobileNumber: '01062343782',
  userId: '고경준_test',
  serverHost: 'joatech.dyndns.org',
  dbName: 'GasMax_Sample',
  dbUser: 'GasMax_Sample',
  dbPass: 'GasMax_Pass',
  port: '2521',
  areaCode: '01',
  areaName: '본사4',
  lastAreaCode: '00',
  employeeCode: '01',
  employeeName: '장동건',
  phoneAreaNumber: '041',
  signPath: '/nc20/JoaAPP_Sign/joatech_db/',
  licenseDate: '99991231',
  joinDate: '2022-10-06 10:29',
  lastLoginDate: '2026-02-09 03:34',
  menuPermission: '1.5\n∑2-1248',
  remark: '000000000000',
  gasType: 'LPG',
  chargeYn: 'Y',
};

const MAIN_MENU = [
  { id: 'cid', label: '거래처 관리', image: '/images/v2/main_cid_btn.png' },
  { id: 'sale', label: '고객센터', image: '/images/v2/main_custcenter_btn.png' },
  { id: 'unpaid', label: '미수금', image: '/images/v2/main_uncollected_btn.png' },
  { id: 'collect', label: '수금 관리', image: '/images/v2/main_collection_btn.png' },
  { id: 'meter', label: '검침 등록', image: '/images/v2/main_register_btn.png' },
];

const BIZ_MENU = [
  { id: 'detail', label: '거래처 상세' },
  { id: 'book', label: '거래장부' },
  { id: 'sale', label: '판매등록' },
  { id: 'read', label: '검침등록' },
  { id: 'collect', label: '수금등록' },
  { id: 'safety', label: '안전점검' },
];

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

function formatDateLabel(date) {
  if (!date) return '';
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
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

export default function App() {
  const [page, setPage] = useState('login');
  const [showPassword, setShowPassword] = useState(false);
  const [loginId, setLoginId] = useState('');
  const [loginPw, setLoginPw] = useState('');
  const [remember, setRemember] = useState(false);
  const [keyword, setKeyword] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [searchLoading, setSearchLoading] = useState(false);
  const [searchError, setSearchError] = useState('');
  const [nextPage, setNextPage] = useState(2);
  const [hasMore, setHasMore] = useState(false);
  const [debugInfo, setDebugInfo] = useState('');
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [bizMenuOpen, setBizMenuOpen] = useState(false);
  const [bizMenuCustomer, setBizMenuCustomer] = useState(null);
  const [bookCustomer, setBookCustomer] = useState(null);
  const [bookMode, setBookMode] = useState('weight');
  const [bookStartDate, setBookStartDate] = useState(() => subtractDays(new Date(), 30));
  const [bookEndDate, setBookEndDate] = useState(() => new Date());
  const [bookRecords, setBookRecords] = useState([]);
  const [bookSummary, setBookSummary] = useState({ totalRowCount: 0, carriedOverAmount: '0', totalNowMonthAmount: '0' });
  const [bookError, setBookError] = useState('');
  const [calendarOpen, setCalendarOpen] = useState(null);
  const longPressTimer = useRef(null);

  const appVersion = useMemo(() => 'v2026.02', []);

  const handleLogin = () => {
    if (!loginId || !loginPw) {
      window.alert('아이디와 비밀번호를 입력하세요.');
      return;
    }
    setPage('main');
  };

  const handleJoin = () => {
    window.alert('회원가입 신청 화면은 추후 연결 예정입니다.');
  };

  const handleSearch = async () => {
    if (!keyword.trim()) {
      setSearchError('검색어를 입력해주세요.');
      setSearchResults([]);
      return;
    }

    setSearchLoading(true);
    setSearchError('');
    setDebugInfo('');
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
        setDebugInfo(result.raw || '');
        setSearchResults([]);
        return;
      }

      const items = normalizeItems(result);
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
        setDebugInfo(result.raw || '');
        return;
      }

      const items = normalizeItems(result);
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
        setDebugInfo(result.raw || '');
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

  const loadBook = async (customer = bookCustomer, start = bookStartDate, end = bookEndDate) => {
    if (!customer) return;
    setSearchLoading(true);
    setBookError('');
    try {
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
        setDebugInfo(result.raw || '');
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
      setBookError('거래장부 조회 중 오류가 발생했습니다.');
    } finally {
      setSearchLoading(false);
    }
  };

  const handleBizMenuClick = (menu) => {
    setBizMenuOpen(false);
    if (!bizMenuCustomer) return;
    if (menu.id === 'detail') {
      handleCustomerClick(bizMenuCustomer);
      return;
    }
    if (menu.id === 'book') {
      setBookCustomer(bizMenuCustomer);
      setPage('book');
      loadBook(bizMenuCustomer, bookStartDate, bookEndDate);
      return;
    }
    window.alert(`${bizMenuCustomer.customerName} - ${menu.label} 화면은 추후 연결 예정입니다.`);
  };

  const bindLongPress = (customer) => ({
    onPointerDown: () => {
      longPressTimer.current = setTimeout(() => {
        openBizMenu(customer);
      }, 600);
    },
    onPointerUp: () => {
      if (longPressTimer.current) clearTimeout(longPressTimer.current);
    },
    onPointerLeave: () => {
      if (longPressTimer.current) clearTimeout(longPressTimer.current);
    },
  });

  const renderSearchResults = () => {
    if (searchError) {
      return (
        <div className="result-message error">
          {searchError}
          {debugInfo ? <pre className="debug-raw">{debugInfo}</pre> : null}
        </div>
      );
    }

    if (searchResults.length === 0) return null;

    return (
      <div className="result-list">
        {searchResults.map((item) => (
          <button
            key={`${item.areaCode}-${item.customerCode}`}
            type="button"
            className="result-item"
            onClick={() => handleCustomerClick(item)}
            {...bindLongPress(item)}
          >
            <img src={`/images/lbl_customer_type_${item.customerType}.png`} alt="구분" />
            <div className="result-info">
              <div className="result-title">
                <span className={item.customerStatusCode === '0' ? 'name' : 'name inactive'}>
                  {item.customerName}
                </span>
                {item.customerStatusName && <span className="status">{item.customerStatusName}</span>}
              </div>
              <div className="result-phone">{item.phoneNumber} {item.mobileNumber}</div>
              <div className="result-address">{item.address1} {item.address2}</div>
            </div>
          </button>
        ))}
        {hasMore && (
          <button type="button" className="more-button" onClick={handleMore}>
            검색결과 20건 더보기
          </button>
        )}
      </div>
    );
  };

  const renderCustomerDetailModal = () => {
    if (!detailModalOpen || !selectedCustomer) return null;
    return (
      <div className="modal-backdrop" onClick={() => setDetailModalOpen(false)}>
        <div className="modal" onClick={(event) => event.stopPropagation()}>
          <div className="modal-header">
            <span>{selectedCustomer.customerName}</span>
            <button type="button" className="ghost" onClick={() => setDetailModalOpen(false)}>
              닫기
            </button>
          </div>
          <div className="detail-grid modal-detail">
            <div><span>거래처 코드</span><strong>{selectedCustomer.customerCode}</strong></div>
            <div><span>영업소 코드</span><strong>{selectedCustomer.areaCode}</strong></div>
            <div><span>전화</span><strong>{selectedCustomer.phoneNumber}</strong></div>
            <div><span>휴대폰</span><strong>{selectedCustomer.mobileNumber}</strong></div>
            <div className="detail-address">
              <span>주소</span><strong>{selectedCustomer.address1} {selectedCustomer.address2}</strong>
            </div>
          </div>
          <button className="primary modal-cta" type="button" onClick={() => setDetailModalOpen(false)}>
            확인
          </button>
        </div>
      </div>
    );
  };

  const renderHardcodedInfo = () => (
    <div className="hardcoded-info">
      <div className="hardcoded-title">하드코딩 접속정보</div>
      <div className="hardcoded-grid">
        <div><span>UUID</span><strong>{HARDCODED_INFO.uuid}</strong></div>
        <div><span>HP_SEQ</span><strong>{HARDCODED_INFO.areaSeq}</strong></div>
        <div><span>상태</span><strong>{HARDCODED_INFO.grantState}</strong></div>
        <div><span>모델</span><strong>{HARDCODED_INFO.model}</strong></div>
        <div><span>핸드폰</span><strong>{HARDCODED_INFO.mobileNumber}</strong></div>
        <div><span>로그인ID</span><strong>{HARDCODED_INFO.userId}</strong></div>
        <div><span>서버</span><strong>{HARDCODED_INFO.serverHost}</strong></div>
        <div><span>DB명</span><strong>{HARDCODED_INFO.dbName}</strong></div>
        <div><span>DB계정</span><strong>{HARDCODED_INFO.dbUser}</strong></div>
        <div><span>DB비번</span><strong>{HARDCODED_INFO.dbPass}</strong></div>
        <div><span>포트</span><strong>{HARDCODED_INFO.port}</strong></div>
        <div><span>영업소코드</span><strong>{HARDCODED_INFO.areaCode}</strong></div>
        <div><span>영업소명</span><strong>{HARDCODED_INFO.areaName}</strong></div>
        <div><span>마지막영업소</span><strong>{HARDCODED_INFO.lastAreaCode}</strong></div>
        <div><span>사원코드</span><strong>{HARDCODED_INFO.employeeCode}</strong></div>
        <div><span>사원명</span><strong>{HARDCODED_INFO.employeeName}</strong></div>
        <div><span>지역번호</span><strong>{HARDCODED_INFO.phoneAreaNumber}</strong></div>
        <div><span>서명경로</span><strong>{HARDCODED_INFO.signPath}</strong></div>
        <div><span>라이선스</span><strong>{HARDCODED_INFO.licenseDate}</strong></div>
        <div><span>가입일</span><strong>{HARDCODED_INFO.joinDate}</strong></div>
        <div><span>최종로그인</span><strong>{HARDCODED_INFO.lastLoginDate}</strong></div>
        <div><span>권한</span><strong>{HARDCODED_INFO.menuPermission}</strong></div>
        <div><span>메모</span><strong>{HARDCODED_INFO.remark}</strong></div>
        <div><span>가스타입</span><strong>{HARDCODED_INFO.gasType}</strong></div>
        <div><span>유료여부</span><strong>{HARDCODED_INFO.chargeYn}</strong></div>
      </div>
    </div>
  );

  if (page === 'book') {
    return (
      <div className="app-shell">
        <section className="page book-page">
          <header className="main-header">
            <button className="ghost" type="button" onClick={() => setPage('main')}>이전</button>
            <div className="title">거래장부<span className="pill">{bookMode === 'volume' ? '체적' : '일반'}</span></div>
            <button className="ghost" type="button" onClick={() => loadBook()}>조회</button>
          </header>

          <main className="main-body">
            <div className="book-card">
              <div>
                <div className="detail-title">{bookCustomer?.customerName || '거래처'}</div>
                <div className="detail-sub">{bookCustomer?.customerCode}</div>
              </div>
              <div className="book-filters">
                <label>
                  시작일
                  <button className="date-button" type="button" onClick={() => setCalendarOpen('start')}>
                    {formatDateLabel(bookStartDate)}
                  </button>
                </label>
                <label>
                  종료일
                  <button className="date-button" type="button" onClick={() => setCalendarOpen('end')}>
                    {formatDateLabel(bookEndDate)}
                  </button>
                </label>
              </div>
              <button className="primary" type="button" onClick={() => loadBook(bookCustomer, bookStartDate, bookEndDate)}>
                거래장부 조회
              </button>
            </div>

            <div className="book-summary">
              <div><span>총 건수</span><strong>{bookSummary.totalRowCount}</strong></div>
              {bookMode === 'volume' ? (
                <div><span>당월금액</span><strong>{bookSummary.totalNowMonthAmount}</strong></div>
              ) : (
                <div><span>이월잔액</span><strong>{bookSummary.carriedOverAmount}</strong></div>
              )}
            </div>

            {bookError && <div className="result-message error">{bookError}</div>}

            <div className="book-list">
              {bookRecords.map((row, idx) => (
                <div key={`${row.collectDate || row.readMeterDate}-${row.sequenceNumber}-${idx}`} className="book-row">
                  {bookMode === 'volume' ? (
                    <>
                      <div className="book-row-top">
                        <span>{row.readMeterDate}</span>
                        <span className="book-type">검침</span>
                      </div>
                      <div className="book-title">당검 {row.nowMonthReadMeter} / 사용 {row.useQuantity}</div>
                      <div className="book-meta">
                        <span>단가 {row.price}</span>
                        <span>금액 {row.nowMonthAmount}</span>
                        <span>수납 {row.collectDate || '미수'}</span>
                      </div>
                      <div className="book-meta">
                        <span>미납 {row.unpaidAmount}</span>
                        <span>비고 {row.remark}</span>
                      </div>
                    </>
                  ) : (
                    <>
                      <div className="book-row-top">
                        <span>{row.collectDate}</span>
                        <span className="book-type">{row.typeCode}</span>
                      </div>
                      <div className="book-title">{row.itemName}</div>
                      <div className="book-meta">
                        <span>공급액 {row.amount}</span>
                        <span>입금액 {row.collectAmount}</span>
                        <span>미수 {row.unpaidAmount}</span>
                      </div>
                      <div className="book-meta">
                        <span>합계 {row.totalAmount}</span>
                        <span>잔액 {row.remainAmount}</span>
                      </div>
                    </>
                  )}
                </div>
              ))}
              {bookRecords.length === 0 && !bookError && (
                <div className="result-message">거래장부 결과가 없습니다.</div>
              )}
            </div>
          </main>
        </section>

        {calendarOpen && (
          <div className="modal-backdrop" onClick={() => setCalendarOpen(null)}>
            <div className="modal" onClick={(event) => event.stopPropagation()}>
              <div className="modal-header">
                <span>{calendarOpen === 'start' ? '시작일 선택' : '종료일 선택'}</span>
                <button type="button" className="ghost" onClick={() => setCalendarOpen(null)}>닫기</button>
              </div>
              <div className="calendar-wrap">
                <Calendar
                  locale="ko-KR"
                  selectRange={false}
                  onChange={(value) => {
                    const nextDate = Array.isArray(value) ? value[0] : value;
                    if (!nextDate) return;
                    if (calendarOpen === 'start') {
                      setBookStartDate(nextDate);
                      if (nextDate > bookEndDate) setBookEndDate(nextDate);
                    } else {
                      setBookEndDate(nextDate);
                      if (nextDate < bookStartDate) setBookStartDate(nextDate);
                    }
                    setCalendarOpen(null);
                  }}
                  value={calendarOpen === 'start' ? bookStartDate : bookEndDate}
                />
              </div>
              <button className="primary modal-cta" type="button" onClick={() => setCalendarOpen(null)}>확인</button>
            </div>
          </div>
        )}

        {searchLoading && (
          <div className="loader-overlay">
            <div className="loader-card">
              <div className="loader-spinner" />
              거래장부 조회 중...
            </div>
          </div>
        )}
      </div>
    );
  }

  return (
    <div className="app-shell">
      {page === 'login' ? (
        <section className="page login-page">
          <header className="login-header"><h1>가스 경영 2025</h1></header>
          <main className="login-main">
            <div className="logo-card"><img src="/img/222.png" alt="앱 로고" /></div>
            <div className="login-card">
              <div className="login-field-group">
                <label className="login-field">
                  <span>아이디</span>
                  <input type="text" placeholder="아이디를 입력하세요" value={loginId} onChange={(event) => setLoginId(event.target.value)} />
                </label>
                <label className="login-field">
                  <span>비밀번호</span>
                  <div className="password-row">
                    <input type={showPassword ? 'text' : 'password'} placeholder="비밀번호를 입력하세요" value={loginPw} onChange={(event) => setLoginPw(event.target.value)} autoComplete="off" />
                    <button type="button" className="pw-toggle" onClick={() => setShowPassword((prev) => !prev)}>
                      <i className={showPassword ? 'fa-regular fa-eye-slash' : 'fa-regular fa-eye'} />
                    </button>
                  </div>
                </label>
              </div>
              <label className="remember">
                <input type="checkbox" checked={remember} onChange={(event) => setRemember(event.target.checked)} />
                로그인 정보 저장
              </label>
              <div className="login-actions">
                <button className="primary" type="button" onClick={handleLogin}>로그인</button>
                <button className="secondary" type="button" onClick={handleJoin}>가입신청</button>
              </div>
            </div>
            {renderHardcodedInfo()}
            <div className="app-version">{appVersion}</div>
          </main>
          <footer className="footer"><span>Copyright © 2026 조아테크</span><span>고객지원 <a href={`tel:${SUPPORT_PHONE}`}>{SUPPORT_PHONE}</a></span></footer>
        </section>
      ) : (
        <section className="page main-page">
          <header className="main-header">
            <button className="ghost" type="button" onClick={() => setPage('login')}>종료</button>
            <div className="title">가스 경영 2025<span className="pill">운영</span></div>
            <button className="ghost" type="button" onClick={() => window.alert('설정 화면은 추후 연결 예정입니다.')}>설정</button>
          </header>
          <main className="main-body">
            <div className="search-card">
              <input type="search" placeholder="거래처 검색" value={keyword} onChange={(event) => setKeyword(event.target.value)} onKeyDown={(event) => { if (event.key === 'Enter') handleSearch(); }} />
              <button className="primary" type="button" onClick={handleSearch}>거래처 검색</button>
            </div>
            {renderSearchResults()}
            <div className="menu-grid">
              {MAIN_MENU.map((item) => (
                <button key={item.id} type="button" className="menu-item" onClick={() => handleMenu(item)}>
                  <img src={item.image} alt={item.label} />
                  <span>{item.label}</span>
                </button>
              ))}
            </div>
          </main>
          <footer className="footer footer-dark"><span>Copyright © 2026 조아테크</span><span>고객지원 <a href={`tel:${SUPPORT_PHONE}`}>{SUPPORT_PHONE}</a></span></footer>
        </section>
      )}

      {bizMenuOpen && (
        <div className="modal-backdrop" onClick={() => setBizMenuOpen(false)}>
          <div className="modal" onClick={(event) => event.stopPropagation()}>
            <div className="modal-header">
              <span>{bizMenuCustomer?.customerName || '거래처'}</span>
              <button type="button" className="ghost" onClick={() => setBizMenuOpen(false)}>닫기</button>
            </div>
            <div className="modal-list">
              {BIZ_MENU.map((menu) => (
                <button key={menu.id} type="button" onClick={() => handleBizMenuClick(menu)}>
                  {menu.label}
                </button>
              ))}
            </div>
          </div>
        </div>
      )}

      {renderCustomerDetailModal()}

      {searchLoading && (
        <div className="loader-overlay">
          <div className="loader-card">
            <div className="loader-spinner" />
            거래처 조회 중...
          </div>
        </div>
      )}
    </div>
  );
}
