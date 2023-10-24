import { Header } from '../../components/organisms/Header'
import Sidebar from '../../components/organisms/Sidebar'
import {
  Box,
  Button,
  CircularProgress,
  Typography,
  styled,
} from '@mui/material'
import HomeTemplate from '../../components/templates/HomeTemplate'
import { TransferStatusHeader } from '../../components/organisms/TransferStatusHeader'
import { CollapsablePaymentCard } from '../../components/organisms/CollapsablePaymentCard'
import theme from '../../utils/themes/theme'
import Image from '../../components/atoms/image'
import HOMEPAGE_IMG from '../../../public/assets/image/mobile.svg'
import './style.css'
import {
  HP_BUTTON,
  HP_HEADING,
  HP_TEXTLINE_ONE,
  HP_TEXTLINE_TWO,
} from '../../utils/constants'
import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import { API } from '../../services/api/api'

const HomePageFlow = () => {
  const navigate = useNavigate()
  const [loading, setLoading] = useState(true)
  const [homepageData, setHomepageData] = useState<boolean>(false)
  const [transferData, setTransferData] = useState({
    status: '',
    amountSent: 0,
    amountReceived: 0,
    fromCurrency: '',
    toCurrency: '',
  })
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
  })
  const [recipientData, setRecipientData] = useState({
    firstName: '',
    lastName: '',
  })

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [
          transactionsResponse,
          personalDetailsResponse,
          recipientsResponse,
        ] = await Promise.all([
          API.get('/transactions'),
          API.get('/personal-details'),
          API.get('/recipients'),
        ])

        const transactionData = transactionsResponse.data
        const personalData = personalDetailsResponse.data
        const recipientData = recipientsResponse.data

        if (transactionData.length > 0) {
          const newdata = transactionData[transactionData.length - 1]
          setTransferData(newdata)
          setHomepageData(true)
        }

        if (personalData.length > 0) {
          const newdata = personalData[personalData.length - 1]
          setUserData(newdata)
        }

        if (recipientData.length > 0) {
          const newdata = recipientData[recipientData.length - 1]
          setRecipientData(newdata)
        }

        setLoading(false)
      } catch (error) {
        console.log('Error fetching data:', error)
        setLoading(false)
      }
    }

    fetchData()
    const dataFetchInterval = setInterval(fetchData, 1000)

    return () => {
      clearInterval(dataFetchInterval)
    }
  }, [])

  return (
    <>
      {loading ? (
        <Box className="center-container">
          <CircularProgress />
        </Box>
      ) : homepageData ? (
        <HomeTemplate
          sidebarNode={
            <Box width={'100%'}>
              <Sidebar showDetail={true} />
            </Box>
          }
          headerNode={<Header width="100%" />}
          contentNode={
            <Box>
              <Box className="OuterBox">
                <Box className="StackStyle">
                  <Typography variant="h1">{HP_HEADING}</Typography>
                  <SendMoneyButton onClick={() => navigate('/send-money')}>
                    <Typography variant="body2">{HP_BUTTON}</Typography>
                  </SendMoneyButton>
                </Box>
                <Box className="InnerBox">
                  <TransferStatusHeader
                    name={`${recipientData.firstName} ${recipientData.lastName}`}
                    amountSend={`${transferData.amountSent} ${transferData.fromCurrency}`}
                    amountReceived={
                      transferData.amountReceived
                        ? `${transferData.amountReceived.toFixed(2)} ${
                            transferData.toCurrency
                          }`
                        : ''
                    }
                    dropdownBoxTop="5.3rem"
                    width="78vw"
                    dropdownContent={
                      <Box className="DropdownStyle">
                        <CollapsablePaymentCard
                          name={`${userData.firstName} ${userData.lastName} (YOU)`}
                          width="100%"
                        />
                      </Box>
                    }
                  />
                </Box>
              </Box>
            </Box>
          }
        ></HomeTemplate>
      ) : (
        <HomeTemplate
          sidebarNode={
            <Box width={'100%'}>
              <Sidebar showDetail={false} />
            </Box>
          }
          headerNode={<Header width="100%" />}
          contentNode={
            <Box>
              <Box className="TopBox">
                <Box className="StackStyle">
                  <Typography variant="h1">{HP_HEADING}</Typography>
                  <SendMoneyButton onClick={() => navigate('/send-money')}>
                    <Typography variant="body2">{HP_BUTTON}</Typography>
                  </SendMoneyButton>
                </Box>
                <Box className="ContentBoxStyle">
                  <Box className="ImageStyle">
                    <Image source={HOMEPAGE_IMG}></Image>
                  </Box>
                  <Typography
                    variant="body1"
                    color={theme.palette.textColor.lowEmphasis}
                  >
                    {HP_TEXTLINE_ONE}
                  </Typography>
                  <Typography
                    variant="body1"
                    color={theme.palette.textColor.lowEmphasis}
                  >
                    {HP_TEXTLINE_TWO}
                  </Typography>
                </Box>
              </Box>
            </Box>
          }
        ></HomeTemplate>
      )}
    </>
  )
}
export default HomePageFlow

const SendMoneyButton = styled(Button)({
  width: '159px',
  height: '56px',
  borderRadius: '56px',
  color: theme.palette.textColor.contrastText,
  backgroundColor: theme.palette.primary.primary500,
  '&:hover': {
    backgroundColor: theme.palette.primary.primary300,
  },
  textTransform: 'capitalize',
})
