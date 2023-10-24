import React, { useEffect, useState } from 'react'
import Typography from '../../atoms/Typography'
import theme from '../../../utils/themes/theme'
import CustomTextField from '../../atoms/Textfield'
import Button from '../../atoms/button'
import {
  DIDNT_RECEIVE_CODE,
  ENTER_6_DIGIT_CODE,
  ENTER_YOUR_CODE,
  SUBMIT_BUTTON,
  WE_SENT_IT_TO,
} from '../../../utils/constants'
import styled from '@emotion/styled'
import { Box, Grid, Stack } from '@mui/material'
import { useData } from '../../../Context/UserContext'

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

const StyledTextField = styled(CustomTextField)(() => ({
  height: '60px',
  '& .MuiFormLabel-root': {
    color: theme.palette.textColor.lowEmphasis,
  },
  '@media (min-width: 800px)': {
    width: '516px',
  },
  '@media (max-width: 600px)': {
    width: '100%',
  },
  borderRadius: '8px',
}))

const StyledButtonGrid = styled(Grid)`
  width: 651px;
`

const StyledStack = styled(Stack)`
  margin: 10px 0 50px;
`

interface AuthenticationAndOtpProps {
  width?: string
  height?: string
  onClick?: () => void
  handleOtp?: () => void
}

const AuthenticationAndOtp = ({
  width,
  height,
  onClick,
  handleOtp,
}: AuthenticationAndOtpProps) => {
  const { data, updateData } = useData()
  const [inputValue, setInputValue] = useState<string>('')
  const [error, setError] = useState<string>('')
  const [phoneNumber, setPhoneNumber] = useState<string>(data.full_number)

  useEffect(() => {
    const inputString = data.full_number.replace(/\s/g, '')

    const segment1 = inputString.slice(0, 6)
    const segment2 = inputString.slice(6, 10)
    const segment3 = inputString.slice(10)

    const result = [segment1, segment2, segment3].join(' ')
    updateData({ split_number: result })
    setPhoneNumber(result)
  }, [])

  const handleValueChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = event.target.value.replace(/[^0-9]/g, '')
    setInputValue(newValue)

    if (newValue.length > 6) {
      setError('Code must be exactly 6 digits')
    } else {
      setError('')
    }

    setDisabled(newValue.length === 6 && /^\d+$/.test(newValue))
  }

  const [disabled, setDisabled] = useState<boolean>(true)

  return (
    <div>
      <Grid width={width} height={height}>
        <Typography variant="h1" color={theme.palette.textColor.highEmphasis}>
          {ENTER_6_DIGIT_CODE}
        </Typography>
        <StyledStack>
          <Typography
            variant="body3"
            color={theme.palette.textColor.mediumEmphasis}
          >
            {WE_SENT_IT_TO} {phoneNumber}
          </Typography>
        </StyledStack>
        <StyledTextField
          label={ENTER_YOUR_CODE}
          value={inputValue}
          onChange={handleValueChange}
          data-testid="OTP-textField"
        />
        {error && (
          <Typography variant="underlineText" color={'red'}>
            {error}
          </Typography>
        )}
        <Box sx={{ paddingTop: '20px' }}>
          <Typography
            variant="underlineText"
            color={theme.palette.primary.primary500}
            sx={{
              cursor: 'pointer',
              textDecoration: 'underline',
            }}
            onClick={handleOtp}
          >
            {DIDNT_RECEIVE_CODE}
          </Typography>
        </Box>
      </Grid>
      <StyledButtonGrid display="flex" justifyContent="flex-end">
        <StyledButton
          disabled={!disabled}
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

export default AuthenticationAndOtp
