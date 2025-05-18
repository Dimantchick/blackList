import {BooleanInput, Create, SimpleForm, TextInput,} from 'react-admin';


export const NetworksCreate = () => {
    return (
        <Create resource="networks" redirect={"list"}>
            <SimpleForm
            >
                <TextInput
                    source="network"
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
                    defaultValue={true}
                    label="Добавлена вручную"
                />
                <BooleanInput
                    source="active"
                    defaultValue={true}
                    label="Активна"
                />
                <BooleanInput
                    source="imported"
                    defaultValue={false}
                    disabled
                    label="Имортированная"
                />
            </SimpleForm>
        </Create>
    );
}