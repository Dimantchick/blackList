import {
    Create,
    SimpleForm,

        TextInput,    
    
        BooleanInput,    
    


} from 'react-admin';


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
                />
                <BooleanInput 
                source="manual"
                defaultValue={true}
                disabled
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