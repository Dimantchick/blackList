import {Create, required, SimpleForm, TextInput,} from 'react-admin';
import {validateDomain} from './validateDomain';


export const DomainsCreate = () => {
    return (
        <Create resource="domains" redirect={"list"}>
            <SimpleForm
            >
                <TextInput
                    source="domain"
                    validate={[required(), validateDomain()]} // Обязательное поле + валидация
                />
                <TextInput
                    source="comment"
                    defaultValue={""}
                    parse={value => value === null ? "" : value}
                    multiline={true}
                />
            </SimpleForm>
        </Create>
    );
}