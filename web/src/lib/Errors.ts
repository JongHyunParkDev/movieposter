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
};

export const ApiMessage = {
    // client
    [ApiCode.UNKNOWN]: '에러가 발생하였습니다.',
    [ApiCode.REQUEST_FAILED]: '요청에 실패하였습니다.',

    // server
    [ApiCode.GENERIC]: '에러가 발생하였습니다.',
    [ApiCode.NOT_FOUND]: '내용(파일)을 찾을 수 없습니다.',
};
