import * as React from 'react';
import { useState } from 'react';
import {
    useDataProvider,
    useNotify,
    Title,
} from 'react-admin';
import { 
    TextField as MuiTextField,
    List,
    ListItem,
    ListItemText,
    Paper,
    Box,
    Button as MuiButton,
    useMediaQuery,
    Theme,
} from '@mui/material';
import { CustomDataProvider } from 'src/dataProvider';

export const ResolveDomain = () => {
    const [domain, setDomain] = useState('');
    const [addresses, setAddresses] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [isPressed, setIsPressed] = useState(false); // Добавляем состояние для анимации
    const dataProvider = useDataProvider<CustomDataProvider>();
    const notify = useNotify();
    const isSmallScreen = useMediaQuery((theme: Theme) => theme.breakpoints.down('sm'));

    const handleResolve = () => {
        if (!domain) {
            notify('Please enter a domain', { type: 'warning' });
            return;
        }

        setLoading(true);
        dataProvider.customGet('resolve', { domain })
            .then(({ data }) => {
                if (Array.isArray(data)) {
                    setAddresses(data);
                } else {
                    notify('Invalid response format', { type: 'error' });
                    setAddresses([]);
                }
            })
            .catch(error => {
                notify(`Error: ${error.message}`, { type: 'error' });
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
        <Box sx={{ maxWidth: 600, margin: '0 auto', p: isSmallScreen ? 1 : 3 }}>
            <Title title="Resolve Domain" />
            <Paper sx={{ p: isSmallScreen ? 2 : 3, mb: 3 }}>
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
                        label="Domain"
                        value={domain}
                        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setDomain(e.target.value)}
                        onKeyPress={handleKeyPress}
                        disabled={loading}
                        variant="outlined"
                        size="small"
                        fullWidth
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
                        Resolve
                    </MuiButton>
                </Box>

                {addresses.length > 0 && (
                    <Paper variant="outlined">
                        <List dense>
                            {addresses.map((address, index) => (
                                <ListItem key={index}>
                                    <ListItemText primary={address} />
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                )}
            </Paper>
        </Box>
    );
};