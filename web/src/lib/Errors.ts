export class ApiError extends Error {
  code: string;

  constructor(messsage: string, code: string) {
    super(messsage);
    this.code = code;
  }
}

export interface BackEndError {
  code: string;
  message: string;
}

export const ApiCode = {
  // client
  UNKNOWN: '2000',
  REQUEST_FAILED: '2001',

  // server
  GENERIC: '1000',
  NOT_FOUND: '1001',
  INTERNAL_ERROR: '1002',

  INVALID_REQUEST: '1100',
  INVALID_ARGUMENT: '1101',

  FAILED_UPLOAD: '1200',
  NOT_APPLY_FILE: '1201',
  FAILED_FILE_DELETE: '1202',
  FAILED_POSTER_DETAIL: '1203',
};

export const ApiMessage = {
  // client
  [ApiCode.UNKNOWN]: '에러가 발생하였습니다.',
  [ApiCode.REQUEST_FAILED]: '요청에 실패하였습니다.',

  // server
  [ApiCode.GENERIC]: '에러가 발생하였습니다.',
  [ApiCode.NOT_FOUND]: '내용(파일)을 찾을 수 없습니다.',
  [ApiCode.INTERNAL_ERROR]: '서버 내부 에러 입니다.',
  [ApiCode.INVALID_REQUEST]: '잘못된 요청입니다.',
  [ApiCode.INVALID_ARGUMENT]: '잘못된 파라미터입니다.',
  [ApiCode.FAILED_UPLOAD]: '파일 업로드에 실패했습니다',
  [ApiCode.NOT_APPLY_FILE]: '지원하지 않는 파일 형식입니다',
  [ApiCode.FAILED_FILE_DELETE]: '파일 삭제에 실패했습니다.',
  [ApiCode.FAILED_POSTER_DETAIL]: '상세 내역이 이미 존재합니다.',
};
