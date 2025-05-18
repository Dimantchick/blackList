import * as React from 'react';
import {useState} from 'react';
import {Title, useDataProvider, useNotify,} from 'react-admin';
import {
    Box,
    Button as MuiButton,
    List,
    ListItem,
    ListItemText,
    Paper,
    TextField as MuiTextField,
    Theme,
    Typography,
    useMediaQuery,
} from '@mui/material';
import {CustomDataProvider} from 'src/dataProvider';

export const ResolveDomain = () => {
    const [domain, setDomain] = useState('');
    const [addresses, setAddresses] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [isPressed, setIsPressed] = useState(false);
    const dataProvider = useDataProvider<CustomDataProvider>();
    const notify = useNotify();
    const isSmallScreen = useMediaQuery((theme: Theme) => theme.breakpoints.down('sm'));

    const normalizeDomain = (input: string): string => {
        try {
            let normalized = input.replace(/^https?:\/\//i, '');
            normalized = normalized.split('/')[0];
            normalized = normalized.split(':')[0];
            return normalized.trim();
        } catch {
            return input.trim();
        }
    };

    const handleResolve = () => {
        const normalizedDomain = normalizeDomain(domain);
        if (!normalizedDomain) {
            notify('Пожалуйста, введите домен', {type: 'warning'});
            return;
        }
        setDomain(normalizedDomain);
        setLoading(true);
        dataProvider.customGet('resolve', {domain: normalizedDomain})
            .then(({data}) => {
                if (Array.isArray(data)) {
                    setAddresses(data);
                } else {
                    notify('Неверный формат ответа', {type: 'error'});
                    setAddresses([]);
                }
            })
            .catch(error => {
                notify(`Ошибка: ${error.message}`, {type: 'error'});
                setAddresses([]);
            })
            .finally(() => setLoading(false));
    };

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter') {
            setIsPressed(true);
            setTimeout(() => setIsPressed(false), 200);
            handleResolve();
        }
    };

    return (
        <Box sx={{width: '100%', margin: '0 auto', p: 0}}>
            <Title title="IP Домена"/>
            <Paper sx={{width: '100%', p: isSmallScreen ? 2 : 3, mb: 3}}>
                <Box sx={{
                    padding: '16px',
                    backgroundColor: 'transparent'
                }}>
                    <Typography variant="body2">
                        Получить IP адреса по домену.
                    </Typography>
                </Box>
                <Box
                    sx={{
                        display: 'flex',
                        alignItems: 'flex-end',
                        gap: 2,
                        mb: 3,
                        flexDirection: isSmallScreen ? 'column' : 'row'
                    }}
                >
                    <MuiTextField
                        label="Домен"
                        value={domain}
                        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setDomain(e.target.value)}
                        onKeyPress={handleKeyPress}
                        disabled={loading}
                        variant="outlined"
                        size="small"
                        fullWidth
                        placeholder="например: rutracker.org или https://google.com"
                    />
                    <MuiButton
                        variant="contained"
                        onClick={() => {
                            setIsPressed(true);
                            setTimeout(() => setIsPressed(false), 200);
                            handleResolve();
                        }}
                        disabled={loading}
                        sx={{
                            height: '38px',
                            minWidth: isSmallScreen ? '100%' : '120px',
                            whiteSpace: 'nowrap',
                            mb: 0.5,
                            transform: isPressed ? 'scale(0.98)' : 'scale(1)',
                            transition: 'transform 0.1s ease',
                        }}
                    >
                        Получить
                    </MuiButton>
                </Box>

                {addresses.length > 0 && (
                    <Paper variant="outlined" sx={{marginTop: 2}}>
                        <Typography
                            variant="subtitle1"
                            component="div"
                            sx={{
                                padding: '8px 16px',
                                backgroundColor: (theme) => theme.palette.grey[100],
                                borderBottom: (theme) => `1px solid ${theme.palette.divider}`
                            }}
                        >
                            Распознанные адреса
                        </Typography>
                        <List dense>
                            {addresses.map((address, index) => (
                                <ListItem key={index}>
                                    <ListItemText primary={address}/>
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                )}
            </Paper>
        </Box>
    );
};