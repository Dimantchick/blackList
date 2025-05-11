import {
    Edit,
    SimpleForm,

        TextInput,    
    
        PasswordInput,    
    


} from 'react-admin';


export const UsersEdit = () => {
    return (
        <Edit resource="users" redirect={"list"}>
            <SimpleForm
            >
                <TextInput 
                source="username"
                required
                disabled
                />
                <PasswordInput 
                source="password"
                required
                />
            </SimpleForm>
        </Edit>
    );
}