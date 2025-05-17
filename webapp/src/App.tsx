import {Admin, CustomRoutes, Resource} from "react-admin";
import {amplicodeDarkTheme, amplicodeLightTheme,} from "./themes/amplicodeTheme/amplicodeTheme";
import {dataProvider} from "./dataProvider";
import {NetworksList} from "./networks/NetworksList";
import {NetworksCreate} from "./networks/NetworksCreate";
import {NetworksEdit} from "./networks/NetworksEdit";
import authProvider from "./formLoginAuthProvider";
import {i18nProvider} from "./i18nProvider";
import {UsersList} from "./users/UsersList";
import {UsersEdit} from "./users/UsersEdit";
import {UsersCreate} from "./users/UsersCreate";
import {ResolveDomain} from "./resolve/ResolveDomain";
import { Route } from 'react-router-dom';

export const App = () => {
  return (
    <Admin
      authProvider={authProvider}
      dataProvider={dataProvider}
      lightTheme={amplicodeLightTheme}
      darkTheme={amplicodeDarkTheme} requireAuth={true}
      i18nProvider={i18nProvider}
      disableTelemetry={true}
    >
    <CustomRoutes>
            <Route path="/resolve" element={<ResolveDomain />} />
        </CustomRoutes>
<Resource
          name="networks"

list={NetworksList}
create={NetworksCreate}
edit={NetworksEdit}
        />

    <Resource name="users" 
        edit={UsersEdit} list={UsersList}
        
        recordRepresentation="username"
create={UsersCreate}
      />
</Admin>
  )
};
