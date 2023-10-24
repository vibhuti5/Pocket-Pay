import AvatarDropdown from '../avatarDropdown'
import { Box, styled } from '@mui/material'
import bell from '../../../../public/assets/image/bell.svg'
import theme from '../../../utils/themes/theme'
import Typography from '../../atoms/Typography'
import { dropdownValues } from '../../../utils/constants'
import Icon from '../../atoms/icon'
import { useEffect, useState } from 'react'
import { API } from '../../../services/api/api'

export interface HeaderProps {
  height?: string
  width?: string
  left?: string
  top?: string
}

const StyledBox = styled(Box)({
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'end',
  alignItems: 'center',
  border: '1px',
  boxShadow: '0px 1px 8px 0px',
  backgroundColor: theme.palette.structuralColor.white,
})

const StyledBellIcon = styled(Icon)({
  width: '1.5rem',
  height: '1.5rem',
})

export const Header: React.FC<HeaderProps> = ({ width, height, top, left }) => {
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
  })

  useEffect(() => {
    try {
      API.get('/personal-details').then((response) => {
        if (response) {
          console.log(response.data)
          const data = response.data
          if (data && data.length > 0) {
            const newdata = data[data.length - 1]
            setUserData(newdata)
          }
        }
      })
    } catch (error) {
      console.log('Get Error')
    }
  }, [])

  return (
    <StyledBox
      width={width}
      height={height}
      marginTop={top}
      marginLeft={left}
      data-testid="styled-box"
    >
      <Box display={'flex'} margin={'10px'} alignItems={'center'}>
        <Box marginRight={'20px'} width={'1.5rem'} height={'1.5rem'}>
          <StyledBellIcon src={bell} alt="icon-bell" />
        </Box>

        <Box
          display={'flex'}
          flexDirection={'row'}
          marginLeft={'1.5rem'}
          marginRight={'1.5rem'}
          alignItems={'center'}
          gap={'15px'}
        >
          <AvatarDropdown dropdownOptions={dropdownValues} />
          <Typography
            variant="caption"
            color={theme.palette.textColor.mediumEmphasis}
          >
            {`${userData.firstName} ${userData.lastName}`}
          </Typography>
        </Box>
      </Box>
    </StyledBox>
  )
}
