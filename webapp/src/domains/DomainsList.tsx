import { Box, Typography } from '@mui/material';
import { 
    Datagrid, 
    List, 
    NumberField, 
    TextField, 
    TextInput,
    TopToolbar,
    CreateButton,
} from 'react-admin';

const ListActions = () => (
    <TopToolbar>
        <CreateButton />
    </TopToolbar>
);

export const DomainsList = () => {
    return (
        <List 
            resource="domains"
            filters={[
                <TextInput source="domainContains" label="Поиск по домену" />, 
                <TextInput source="commentContains" label="Поиск по комментарию" />
            ]}
            actions={<ListActions />}
        >
            <Box sx={{ 
                padding: '16px', 
                backgroundColor: 'transparent' 
            }}>
                <Typography variant="body2">
                    Домены, по которым будет регулярно производиться проверка и адреса будут добавлены в маршруты.
                </Typography>
            </Box>
            <Datagrid rowClick="edit">
                <NumberField source="id" />
                <TextField source="domain" />
                <TextField source="comment" 
                sx={{ wordBreak: 'break-word!important', whiteSpace: 'pre-wrap!important' }}
                />
            </Datagrid>
        </List>
    );
};