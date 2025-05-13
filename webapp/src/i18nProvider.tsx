import {mergeTranslations} from "react-admin";
import polyglotI18nProvider from 'ra-i18n-polyglot';

// Импортируем переводы для русского языка
import russianMessages from 'ra-language-russian';

// Кастомные переводы для вашего приложения
const myRussianMessages = mergeTranslations(russianMessages, {
  resources: {
    networks: {
      name: 'Сети',
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

// Создаем провайдер локализации
export const i18nProvider = polyglotI18nProvider(() => myRussianMessages, 'ru');