const GASMAX_SERVER = (import.meta.env.VITE_GASMAX_SERVER || '').replace(/\/$/, '');
const GASMAX_WEBAPP =
  (import.meta.env.VITE_GASMAX_WEBAPP || '').replace(/\/$/, '') ||
  (GASMAX_SERVER ? `${GASMAX_SERVER}/gasapp` : '/gasapp');

const HARDCODED_UUID = 'b28618772c95b3a5';

function buildUrl(path) {
  if (path.startsWith('http')) return path;
  const base = GASMAX_WEBAPP.endsWith('/') ? GASMAX_WEBAPP.slice(0, -1) : GASMAX_WEBAPP;
  return `${base}/${path.replace(/^\//, '')}`;
}

function extractJson(text) {
  const trimmed = text.trim();
  if (trimmed.startsWith('{') && trimmed.endsWith('}')) return trimmed;
  const first = trimmed.indexOf('{');
  const last = trimmed.lastIndexOf('}');
  if (first === -1 || last === -1 || last <= first) return trimmed;
  return trimmed.slice(first, last + 1);
}

async function postJson(path, bodyParts = []) {
  const body = bodyParts.filter(Boolean).join('&');
  const response = await fetch(buildUrl(path), {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
    },
    body,
    credentials: 'include',
  });

  const text = await response.text();
  const candidate = extractJson(text);
  try {
    return JSON.parse(candidate);
  } catch (error) {
    console.error('JSON parse failed', { path, text });
    return { session: 'O', error: 'INVALID_JSON', raw: text };
  }
}

export async function searchCustomers(keyword) {
  const parts = [
    `keyword=${encodeURIComponent(keyword)}`,
    `uuid=${encodeURIComponent(HARDCODED_UUID)}`,
  ];
  return postJson('search_customer_keyword_json.jsp', parts);
}

export async function searchCustomersPage(pageNumber) {
  const parts = [`pageNumber=${encodeURIComponent(pageNumber)}`];
  return postJson('search_customer_paging_json.jsp', parts);
}

export async function fetchCustomerChoice(areaCode, customerCode) {
  const parts = [
    `areaCode=${encodeURIComponent(areaCode)}`,
    `customerCode=${encodeURIComponent(customerCode)}`,
    `uuid=${encodeURIComponent(HARDCODED_UUID)}`,
  ];
  return postJson('search_customer_choice_json.jsp', parts);
}

export async function fetchCustomerBookWeight(customerCode, startDate, endDate) {
  const parts = [
    `customerCode=${encodeURIComponent(customerCode)}`,
    `startDate=${encodeURIComponent(startDate)}`,
    `endDate=${encodeURIComponent(endDate)}`,
    `uuid=${encodeURIComponent(HARDCODED_UUID)}`,
  ];
  return postJson('customer_book_weight_collect_search_json.jsp', parts);
}

export async function fetchCustomerBookVolumeReadMeter(customerCode, startDate, endDate) {
  const parts = [
    `customerCode=${encodeURIComponent(customerCode)}`,
    `startDate=${encodeURIComponent(startDate)}`,
    `endDate=${encodeURIComponent(endDate)}`,
    `uuid=${encodeURIComponent(HARDCODED_UUID)}`,
  ];
  return postJson('customer_book_volume_read_meter_search_json.jsp', parts);
}
