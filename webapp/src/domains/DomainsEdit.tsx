import {Edit, required, SimpleForm, TextInput,} from 'react-admin';
import {validateDomain} from './validateDomain';


export const DomainsEdit = () => {
    return (
        <Edit resource="domains" redirect={"list"}>
            <SimpleForm
            >
                <TextInput
                    source="domain"
                    validate={[required(), validateDomain()]} // Обязательное поле + валидация
                />
                <TextInput
                    source="comment"
                    multiline={true}
                />
            </SimpleForm>
        </Edit>
    );
}