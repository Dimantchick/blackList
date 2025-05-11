import {
    List,
    Datagrid,

        NumberField,    
    
        TextField,    
    

        EditButton,


        DeleteButton,

} from 'react-admin';


export const UsersList = () => {
    return (
        <List resource="users" >
            <Datagrid
                bulkActionButtons={false}
                rowClick="edit"
            >
                <NumberField 
                source="id"
                
                />
                <TextField 
                source="username"
                
                />
            </Datagrid>
        </List>
    );
}