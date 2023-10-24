import React, { useState, useEffect } from 'react'
import StyledTemplate from '../../components/templates/StyledTemplate'
import POCKETPAYLOGO from '../../../public/assets/image/pocketPayLogo.svg'
import Icon from '../../components/atoms/icon'
import { Alert, AlertTitle, Box, Stack } from '@mui/material'
import { SignIn } from '../../components/organisms/Signin'
import './index.css'
import { API } from '../../services/api/api'
import { useNavigate } from 'react-router'

const HeaderLogo = () => {
  return (
    <Box className="LogoIconStyleBox">
      <Icon src={POCKETPAYLOGO}></Icon>
    </Box>
  )
}

interface LoginData {
  email: string;
  password: string;
}

const LoginPage = () => {
  const navigate = useNavigate()
  const [error, setError] = useState<string | null>(null)

  const handleLoginClick = (email: string, password: string) => {
    const data = {
      email: email,
      password: password,
    }

    postData(data)
  }

  useEffect(() => {
    if (error) {
      const timer = setTimeout(() => {
        setError(null)
      }, 2000)
      return () => {
        clearTimeout(timer)
      }
    }
  }, [error])

  async function postData(data: LoginData) {
    try {
      const response = await API.post('/users/signIn', data)
      localStorage.setItem('token', response.data)
      navigate('/your-details')
    } catch (error) {
      setError('An error occurred. Please check your credentials and try again')
    }
  }

  return (
    <div>
      <StyledTemplate
        frontHeader={<HeaderLogo />}
        mainBody={
          <Box className="main">
            <SignIn onClick={handleLoginClick} />
            {error && (
              <Stack className="error-alert" spacing={2}>
                <Alert severity="error">
                  <AlertTitle>Error</AlertTitle>
                  {error}
                </Alert>
              </Stack>
            )}
          </Box>
        }
      />
    </div>
  )
}

export default LoginPage
