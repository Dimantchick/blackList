import {Validator} from 'react-admin';

interface DomainValidationOptions {
    allowSubdomains?: boolean;
    allowIdn?: boolean;
}

const createDomainValidator = (options?: DomainValidationOptions): Validator => {
    return (value: string) => {
        if (!value) return undefined;
        let domainRegex: RegExp;
        if (options?.allowIdn) {
            domainRegex = /^(?!:\/\/)(?:(?!-)[\p{L}\p{N}-]{1,63}(?<!-)\.)+[\p{L}]{2,}$/u;
        } else {
            domainRegex = /^(?!:\/\/)(?:(?!-)[A-Za-z0-9-]{1,63}(?<!-)\.)+[A-Za-z]{2,}$/;
        }
        if (!options?.allowSubdomains) {
            const parts = value.split('.');
            if (parts.length > 2) {
                return 'Поддомены не разрешены';
            }
        }
        if (value.length > 253) {
            return 'Длина домена не должна превышать 253 символов';
        }
        if (!domainRegex.test(value)) {
            return options?.allowIdn
                ? 'Введите корректное доменное имя (например: пример.рф или example.com)'
                : 'Введите корректное доменное имя (например: example.com)';
        }
        return undefined;
    };
};

export const validateDomain = () => createDomainValidator({
    allowSubdomains: true,
    allowIdn: true
});