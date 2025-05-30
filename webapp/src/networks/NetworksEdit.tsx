import {BooleanInput, Edit, SimpleForm, TextInput,} from 'react-admin';


export const NetworksEdit = () => {
    return (
        <Edit resource="networks" redirect={"list"}>
            <SimpleForm
            >
                <TextInput
                    source="network"
                    disabled
                    required
                    label="Адрес/Сеть"
                />
                <TextInput
                    source="comment"
                    defaultValue={""}
                    parse={value => value === null ? "" : value}
                    multiline={true}
                />
                <BooleanInput
                    source="manual"
                    label="Добавлена вручную"
                />
                <BooleanInput
                    source="active"
                    label="Активна"
                />
                <BooleanInput
                    source="imported"
                    disabled
                    label="Имортированная"
                />
                <TextInput
                    source='updated'
                    disabled
                />
            </SimpleForm>
        </Edit>
    );
}