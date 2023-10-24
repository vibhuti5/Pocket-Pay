import { Token } from '../../utils/constants'
import { API } from '../api/api'

export const getToken = async (data: Token) => {
  const response = await API.post(`/user/token`, data)
  return response.data
}
