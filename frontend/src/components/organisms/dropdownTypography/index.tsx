import React, { useEffect, useState } from 'react'
import CustomTextField from '../../atoms/Textfield'
import { Stack, InputAdornment, IconButton, Popover, Grid } from '@mui/material'
import Icon from '../../atoms/icon'
import Typography from '../../atoms/Typography'
import theme from '../../../utils/themes/theme'
import {
  COUNTRY_ICON_ALT,
  DOWN_ARROW_ALT,
  IT_HELPS_US_KEEP,
  MOBILE_LABEL,
  SUBMIT_BUTTON,
  VERIFY_YOUR_PHONE_NUMBER,
} from '../../../utils/constants'
import DownArrowImg from '../../../../public/assets/image/downArrow.svg'
import GBPImg from '../../../../public/assets/image/gbp.svg'
import styled from '@emotion/styled'
import Button from '../../atoms/button'
import { useData } from '../../../Context/UserContext'
import ANDORA from '../../../../public/assets/image/andorra.svg'
import UK from '../../../../public/assets/image/uk.svg'
import AUSTRIA from '../../../../public/assets/image/austria.svg'
import IND from '../../../../public/assets/image/india.svg'

interface DropdownTypographyProps {
  array: CountryProps[]
  width?: string
  height?: string
  onClick?: () => void
}

interface CountryProps {
  id: number
  src: string
  alt: string
  start: string
}

const StyledStack = styled(Stack)`
  margin: 10px 0 50px;
`

const StyledButtonGrid = styled(Grid)`
  width: 651px;
`
const StyledButton = styled(Button)(() => ({
  height: '56px',
  width: '135px',
  color: theme.palette.textColor.contrastText,
  backgroundColor: theme.palette.primary.primary500,
  '&:disabled': {
    backgroundColor: theme.palette.primary.primary100,
    color: theme.palette.textColor.contrastText,
  },
  '&:hover': {
    backgroundColor: theme.palette.primary.primary300,
  },
}))

const StyledTextField = styled(CustomTextField)({
  '& label.Mui-focused': {
    color: theme.palette.textColor.lowEmphasis,
  },
  '& .MuiOutlinedInput-root': {
    '& fieldset': {
      borderColor: theme.palette.iconColor.stroke,
    },
    '&:hover fieldset': {
      borderColor: theme.palette.iconColor.stroke,
    },
    '&.Mui-focused fieldset': {
      borderColor: theme.palette.iconColor.stroke,
    },
  },
})

const DropdownTypography = ({
  array,
  width,
  height,
  onClick,
}: DropdownTypographyProps) => {
  const { data, updateData } = useData()
  const [anchorEl, setAnchorEl] = React.useState<HTMLButtonElement | null>(null)
  const [icon, setIcon] = useState<string>(GBPImg)
  const [countryCode, setCountryCode] = useState<string>('+44')
  const [value, setValue] = useState<string>('')

  useEffect(() => {
    const country = data.country
    if (country === 'AUSTRIA') {
      setIcon(AUSTRIA)
      setCountryCode('+376')
    } else if (country === 'UNITED KINGDOM') {
      setIcon(UK)
      setCountryCode('+44')
    } else if (country === 'ANDORA') {
      setIcon(ANDORA)
      setCountryCode('+43')
    } else if (country === 'INDIA') {
      setIcon(IND)
      setCountryCode('+91')
    }
  }, [])

  const open = Boolean(anchorEl)
  const id = open ? 'simple-popover' : undefined

  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    setAnchorEl(event.currentTarget)
  }

  const handleCountry = (src: string, code: string, country_code: string) => {
    setIcon(src)
    setCountryCode(code)
    setAnchorEl(null)
    updateData({ country_code })
  }

  const isValueValid = () => {
    return /^\d+$/.test(value) && value.length > 9
  }

  const handleValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const inputValue = event.target.value.trim()
    updateData({ full_number: inputValue })
    const number = inputValue.split(' ')
    const phone_number = number[1]
    updateData({ phone_number })

    if (inputValue.startsWith(countryCode)) {
      setValue(inputValue.slice(countryCode.length).trim())
    } else {
      setValue(inputValue)
    }
  }

  return (
    <div>
      <Grid width={width} height={height}>
        <Typography variant="h1" color={theme.palette.textColor.highEmphasis}>
          {VERIFY_YOUR_PHONE_NUMBER}
        </Typography>
        <StyledStack>
          <Typography
            variant="body3"
            color={theme.palette.textColor.mediumEmphasis}
          >
            {IT_HELPS_US_KEEP}
          </Typography>
        </StyledStack>
        <Popover
          id={id}
          open={open}
          anchorEl={anchorEl}
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'left',
          }}
          data-testid="pop-over"
          disableAutoFocus
        >
          {array.map((item) => {
            return (
              <Stack key={item.id} data-testid="pop-over-stack">
                <IconButton
                  data-testid="pop-over-click"
                  onClick={() => handleCountry(item.src, item.start, item.alt)}
                >
                  <Icon
                    src={item.src}
                    alt={item.alt}
                    style={{ marginRight: '10px' }}
                  />
                  <Typography color={theme.palette.textColor.mediumEmphasis}>
                    {item.start}
                  </Typography>
                </IconButton>
              </Stack>
            )
          })}
        </Popover>
        <Stack display="flex" flexDirection="row" width={'100%'}>
          <StyledTextField
            label={MOBILE_LABEL}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <IconButton
                    onClick={handleClick}
                    data-testid="country-select-button"
                  >
                    <Icon src={icon} alt={COUNTRY_ICON_ALT} />
                    <Icon src={DownArrowImg} alt={DOWN_ARROW_ALT} />
                  </IconButton>
                </InputAdornment>
              ),
            }}
            onChange={handleValueChange}
            value={`${countryCode} ${value}`}
            style={{ width: '516px' }}
          ></StyledTextField>
        </Stack>
      </Grid>
      <StyledButtonGrid display="flex" justifyContent="flex-end">
        <StyledButton
          disabled={!isValueValid()}
          onClick={onClick}
          data-testid="submit-button"
        >
          <Typography
            variant="body2"
            color={theme.palette.structuralColor.white}
          >
            {SUBMIT_BUTTON}
          </Typography>
        </StyledButton>
      </StyledButtonGrid>
    </div>
  )
}

export default DropdownTypography
