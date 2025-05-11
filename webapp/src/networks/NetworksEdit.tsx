import {
    Edit,
    SimpleForm,

        TextInput,    
    
        BooleanInput,    
    


} from 'react-admin';


export const NetworksEdit = () => {
    return (
        <Edit resource="networks" redirect={"list"} >
            <SimpleForm
            >
                <TextInput 
                source="network"
                required
                label="Адрес/Сеть"
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
            </SimpleForm>
        </Edit>
    );
}