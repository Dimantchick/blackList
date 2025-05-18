import {Box, Typography} from '@mui/material';
import {
    BooleanField,
    BooleanInput,
    Datagrid,
    List,
    NumberField,
    Pagination,
    PaginationProps,
    TextField,
    TextInput
} from 'react-admin';


export const NetworksList = () => {
    return (
        <List resource="networks" perPage={50} pagination={<CustomPagination/>}
              filters={[<TextInput source="networkContains"/>, <BooleanInput source="manual"/>,
                  <BooleanInput source="active"/>]}>
            <Box sx={{
                padding: '16px',
                backgroundColor: 'transparent'
            }}>
                <Typography variant="body2">
                    Адреса и сети, которые будут работать через VPN.
                </Typography>
            </Box>
            <Datagrid
                rowClick="edit"

            >
                <NumberField
                    source="id"
                    label="Ид"
                    width={50}
                />
                <TextField
                    source="network"
                    label="Адрес/Сеть"
                />
                <TextField
                    source="comment"
                    sx={{wordBreak: 'break-word!important', whiteSpace: 'pre-wrap!important'}}
                />
                <BooleanField
                    source="manual"
                    label="Добавлена вручную"
                />
                <BooleanField
                    source="active"
                    label="Активна"
                />
                <BooleanField
                    source="imported"
                    label="Имортированная"
                />
                <TextField
                    source='updated'
                />
            </Datagrid>
        </List>
    );
}

const CustomPagination = (props: PaginationProps) => (
    <>
        {/* CustomPagination component{" "} */}
        <Pagination rowsPerPageOptions={[10, 25, 50, 100, 200]} {...props} />
    </>
)