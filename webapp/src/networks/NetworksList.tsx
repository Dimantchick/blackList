import {
    BooleanField,
    BooleanInput,
    Datagrid,
    DateField,
    List,
    NumberField,
    Pagination,
    PaginationProps,
    TextField,
    TextInput
} from 'react-admin';


export const NetworksList = () => {
    return (
        <List resource="networks" perPage={50} pagination={<CustomPagination />} filters={[<TextInput source="networkContains"/>,<BooleanInput source="manual"/>,<BooleanInput source="active"/>]} >
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
                <DateField
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