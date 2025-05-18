import {Box, Typography} from '@mui/material';
import {Datagrid, List, NumberField, TextField,} from 'react-admin';


export const UsersList = () => {
    return (
        <List resource="users">
            <Box sx={{
                padding: '16px',
                backgroundColor: 'transparent'
            }}>
                <Typography variant="body2">
                    Пользователи данного портала.
                </Typography>
            </Box>
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