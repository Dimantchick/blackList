import { DataProvider, fetchUtils } from "react-admin";
import springDataProvider from "./springDataProvider";

const baseDataProvider = springDataProvider(
  import.meta.env.VITE_SIMPLE_REST_URL
);

export interface CustomDataProvider extends DataProvider {
  customGet: (resource: string, params: { domain: string }) => Promise<{ data: any }>;
}

export const dataProvider: CustomDataProvider = {
  ...baseDataProvider,
  customGet: (resource, params) => {
    const url = `${import.meta.env.VITE_SIMPLE_REST_URL}/${resource}?domain=${encodeURIComponent(params.domain)}`;
    return fetchUtils.fetchJson(url).then(({ json }) => ({ data: json }));
  }
};