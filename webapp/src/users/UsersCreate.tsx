import {
    Create,
    SimpleForm,

        TextInput,    
    
        PasswordInput,    
    


} from 'react-admin';


export const UsersCreate = () => {
    return (
        <Create resource="users" redirect={"list"}>
            <SimpleForm
            >
                <TextInput 
                source="username"
                required
                />
                <PasswordInput 
                source="password"
                required
                />
            </SimpleForm>
        </Create>
    );
}