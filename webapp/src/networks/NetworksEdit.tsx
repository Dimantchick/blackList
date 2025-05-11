import {
    Edit,
    SimpleForm,

        TextInput,    
    
        BooleanInput,
        DateInput,    
    


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
                <DateInput
                source='updated'
                disabled
                />
            </SimpleForm>
        </Edit>
    );
}