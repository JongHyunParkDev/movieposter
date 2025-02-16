import defaultAxios, {
  AxiosError,
  AxiosPromise,
  AxiosResponse,
  AxiosRequestConfig,
} from 'axios';
import { ApiError, ApiCode, ApiMessage, BackEndError } from './Errors';
import qs from 'qs';
import { useErrorStore } from 'src/stores/ErrorStore';

const errorStore = useErrorStore();

const apiPrefix = process.env.API;
interface CustomAxiosRequestConfig extends AxiosRequestConfig {
  isBinary?: boolean;
  isBlob?: boolean;
  [key: string]: unknown;
}

interface CustomObejct {
  [key: string]: unknown;
}

const axios = defaultAxios.create({
  timeout: 60000,
  withCredentials: true,
  // axios는 기본이 'bracket'인데, 그러면 https://...getList?names[]=a&names[]=b 처럼 request
  // url이 만들어지고, Spring Boot에서는 '['에 대해 다음과 같은 오류를 낸다.
  //   Invalid character found in the request target.
  //   The valid characters are defined in RFC 7230 and RFC 3986
  // repeat을 사용하면 Spring Boot에서 성공적으로 받는다.
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat' }),
});

export const Api = {
  get: (
    url: string,
    params?: CustomObejct,
    config?: CustomAxiosRequestConfig
  ) => {
    params = params ? { params: params } : undefined;
    return apiProcess(
      axios.get(apiPrefix + url, appendAxiosConfig(params, config))
    );
  },

  postWithFiles: (url: string, formData: FormData) => {
    return apiProcess(axios.post(apiPrefix + url, formData));
  },

  post: (
    url: string,
    params?: CustomObejct,
    config?: CustomAxiosRequestConfig
  ) => {
    if (params === undefined) params = {};
    return apiProcess(
      axios.post(apiPrefix + url, appendAxiosConfig(params, config))
    );
  },

  put: (
    url: string,
    params?: CustomObejct,
    config?: CustomAxiosRequestConfig
  ) => {
    if (params === undefined) params = {};
    return apiProcess(
      axios.put(apiPrefix + url, appendAxiosConfig(params, config))
    );
  },

  delete: (
    url: string,
    params?: CustomObejct,
    config?: CustomAxiosRequestConfig
  ) => {
    params = params ? { data: params } : undefined;
    return apiProcess(
      axios.delete(apiPrefix + url, appendAxiosConfig(params, config))
    );
  },
};

function appendAxiosConfig(
  axiosConfig?: CustomObejct,
  config?: CustomAxiosRequestConfig
) {
  for (const key in config)
    if (Object.hasOwnProperty.call(config, key)) {
      if (key === 'isBinary') add('responseType', 'arraybuffer');
      else if (key === 'isBlob') add('responseType', 'blob');
      else if (key !== 'reqConvertSpec' && key !== 'resConvertSpec')
        add(key, config[key] as string);
    }
  return axiosConfig;

  function add(key: string, value: string) {
    if (axiosConfig === undefined) axiosConfig = {};
    axiosConfig[key] = value;
  }
}

function apiProcess(axiosPromise: AxiosPromise) {
  return axiosPromise.then(
    (response: AxiosResponse) => {
      if (response === undefined) return undefined;
      return response.data;
    },
    (error: AxiosError) => {
      console.error(error);

      // client 발생 시점
      if (error.response === undefined) {
        const returnError = new ApiError(
          error.message,
          ApiCode.REQUEST_FAILED.toString()
        );
        return Promise.reject(returnError);
      }

      // server 발생 시점
      if (error.response.data instanceof Object) {
        const data = error.response.data as BackEndError;

        let message = ApiMessage[data.code];
        if (message) {
          if (data.message) message += ' (' + data.message + ')';
        } else {
          message = ApiMessage[ApiCode.UNKNOWN] + ' (' + data.code;
          if (data.message) message += ': ' + data.message;
          message += ')';
        }

        const returnError = new ApiError(
          message,
          data.code ? data.code : ApiCode.UNKNOWN.toString()
        );
        errorStore.addError(returnError.message);
        return Promise.reject(returnError);
      } else {
        errorStore.addError(error.message);
        Promise.reject(new Error(error.message));
      }
    }
  );
}
