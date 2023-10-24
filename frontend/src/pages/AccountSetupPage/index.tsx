import StyledTemplate from '../../components/templates/StyledTemplate'
import Icon from '../../components/atoms/icon'
import pocketPay from '../../../public/assets/image/pocketPayLogo.svg'
import CustomStepper from '../../components/molecules/SteeperWithLabel'
import { Box, Grid } from '@mui/material'
import React, { useEffect, useState } from 'react'
import {
  MOBILE_NUMBER_DROPDOWN,
  Token,
  TokenGen,
  setupHorizontalStepperValues,
} from '../../utils/constants'
import { AccountTypeCard } from '../../components/organisms/AccountTypeCard'
import { CountryRegistration } from '../../components/organisms/CountryRegistration'
import DropdownTypography from '../../components/organisms/dropdownTypography'
import AuthenticationAndOtp from '../../components/organisms/authenticationAndOtp'
import ApproveAnotherWay from '../../components/organisms/approveAnotherWay'
import CreatePassword from '../../components/organisms/createPassword'
import BACKICON from '../../../public/assets/image/leftArrow.svg'
import './AccountSetupPage.css'
import { useData } from '../../Context/UserContext'
import { API } from '../../services/api/api'
import { useNavigate } from 'react-router-dom'
import { useAuth0 } from '@auth0/auth0-react'
import { getToken } from '../../services/user/getToken'
import Button from '../../components/atoms/button'
import CLOSEICON from '../../../public/assets/image/close.svg'

const HeaderLogo = () => {
  return (
    <Box className="LogoIconStyleBox">
      <Icon src={pocketPay}></Icon>
    </Box>
  )
}

const gridItemStyle = {
  width: '516px',
}
const gridItemWithButtonStyle = {
  width: '786px',
  paddingLeft: '135px',
}

const AccountSetupPage = () => {
  const { data, updateData } = useData()
  const navigate = useNavigate()
  const { user, isAuthenticated } = useAuth0()
  const [backIconControl, setBackIconControl] = useState<boolean>(true)
  const [pageValue, setPageValue] = useState<number>(1)
  const [stepperValue, setStepperValue] = useState<number>(2)
  const [loading, setLoading] = useState<boolean>(false)

  useEffect(() => {
    const fetchData = async () => {
      const token = await localStorage.getItem('token')

      if (!token && data.flag === 1) {
        console.log('1')
        navigate('/account-setup')
      } else {
        console.log('2')
        navigate('/account-setup')
        return
      }
      if (isAuthenticated && user) {
        const email = user.email
        const requestData: Token = {
          email: email,
          password: 'password',
        }
        try {
          const response = await getToken(requestData)
          localStorage.setItem('authToken', response)
          TokenGen(response.data.token)
          setLoading(false)
        } catch (error) {
          console.error('Error fetching data:', error)
          setLoading(false)
        }
      } else {
        setLoading(false)
      }
    }
    fetchData()
  }, [isAuthenticated])

  useEffect(() => {
    if (isAuthenticated && user) {
      updateData({ email: user.email })
    }
  }, [isAuthenticated])

  async function postData(data: unknown) {
    try {
      const response = await API.post('/users/token', data)
      localStorage.setItem('token', response.data)
    } catch (error) {}
  }

  const userData = {
    email: data.email,
    password: data.password,
    phoneNumber: data.phone_number,
    countryCode: data.country_code,
  }

  const handlePageNav = (
    stepvalue: number,
    iconVisibility: boolean,
    pageNumber: number
  ) => {
    setStepperValue(stepvalue)
    setBackIconControl(iconVisibility)
    setPageValue(pageNumber)
  }

  const handleComponentBack = (stepvalue: number, pageNumber: number) => {
    if (stepperValue === 3) {
      setStepperValue(stepvalue - 1)
      setPageValue(pageNumber - 1)
    } else if (stepperValue === 4) {
      if (pageNumber === 3) {
        setStepperValue(stepvalue - 1)
      }
      setPageValue(pageNumber - 1)
    } else if (stepperValue === 5) {
      setStepperValue(stepvalue - 1)
      setPageValue(pageNumber - 1)
    }
  }

  return (
    <StyledTemplate
      mainBody={
        loading ? (
          <div>Loading...</div>
        ) : (
          <Grid container className="StyledGrid">
            {pageValue === 1 && (
              <Grid item sx={gridItemStyle}>
                <AccountTypeCard onclick={() => handlePageNav(3, true, 2)} />
              </Grid>
            )}
            {pageValue === 2 && (
              <Grid item sx={gridItemWithButtonStyle}>
                <CountryRegistration
                  comboWidth="480px"
                  onClick={() => handlePageNav(4, true, 3)}
                />
              </Grid>
            )}
            {pageValue === 3 && (
              <Grid item sx={gridItemWithButtonStyle}>
                <DropdownTypography
                  array={MOBILE_NUMBER_DROPDOWN}
                  width="516px"
                  height="68vh"
                  onClick={() => handlePageNav(4, true, 4)}
                />
              </Grid>
            )}
            {pageValue === 4 && (
              <Grid item sx={gridItemWithButtonStyle}>
                <AuthenticationAndOtp
                  width="610px"
                  height="68vh"
                  onClick={() => handlePageNav(5, true, 6)}
                  handleOtp={() => handlePageNav(4, true, 5)}
                />
              </Grid>
            )}
            {pageValue === 5 && (
              <Grid item sx={gridItemStyle}>
                <ApproveAnotherWay onClick={() => handlePageNav(4, true, 3)} />
              </Grid>
            )}
            {pageValue === 6 && (
              <Grid item sx={gridItemWithButtonStyle}>
                <CreatePassword
                  height="68vh"
                  onClick={() => {
                    postData(userData)
                    navigate('/your-details')
                  }}
                />
              </Grid>
            )}
          </Grid>
        )
      }
      frontHeader={<HeaderLogo />}
      middleHeader={
        <Box width={'100%'}>
          <CustomStepper
            presentValue={stepperValue}
            horizontalStepperValues={setupHorizontalStepperValues}
            width="100%"
          />
        </Box>
      }
      buttonIcon={
        <Box className="BoxStyle">
          {backIconControl && (
            <Button
              className="ButtonStyle"
              data-testid="back-button"
              variant="text"
              onClick={() => handleComponentBack(stepperValue, pageValue)}
              disableElevation
              disableTouchRipple
            >
              <Icon src={BACKICON}></Icon>
            </Button>
          )}
        </Box>
      }
      endHeader={
        <Box className="CloseButtonBox">
          <Button
            className="ButtonStyle"
            data-testid="close-button"
            variant="text"
            onClick={() => console.log('Close button Integration ...')}
            disableElevation
            disableTouchRipple
          >
            <Icon src={CLOSEICON}></Icon>
          </Button>
        </Box>
      }
    />
  )
}

export default AccountSetupPage
