import {mergeTranslations} from "react-admin";
import polyglotI18nProvider from 'ra-i18n-polyglot';
import russianMessages from 'ra-language-russian';

const myRussianMessages = mergeTranslations(russianMessages, {
  resources: {
    networks: {
      name: 'Маршруты',
      fields: {
        id: 'Ид',
        network: 'Адрес/сеть',
        comment: 'Комментарий',
        networkContains: 'Адрес содержит',
        manual: 'Ручной режим',
        active: 'Активна',
      },
    },
    users: {
        name: 'Пользователи',
        fields: {
            id: 'Ид',
            username: 'Имя',
            password: 'Пароль',
        }
    },
    domains: {
        name: 'Домены',
        fields: {
            id: 'Ид',
            domain: 'Домен',
            comment: 'Комментарий',
            domainContains: 'Домен содержит',
            commentContains: 'Комментарий содержит',
        }
    },
    resolve: {
        name: 'IP домена',
        fields: {
            resolve: 'Ид',
            domain: 'Домен',
            comment: 'Комментарий',
            domainContains: 'Домен содержит',
            commentContains: 'Комментарий содержит',
        }
    }
  },
  ra: {
    action: {
      create: 'Создать',
      edit: 'Редактировать',
      show: 'Показать',
      delete: 'Удалить',
    },
    page: {
      list: 'Список',
      edit: 'Редактирование',
      create: 'Создание',
    },
  },
});

export const i18nProvider = polyglotI18nProvider(() => myRussianMessages, 'ru');