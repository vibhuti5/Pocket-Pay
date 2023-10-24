import axios from 'axios'
import { BASE_URL } from './api/api'

export const GetJWTToken = async (email: string, password: string) => {
  try {
    const response = await axios.post(BASE_URL + '/users/token', {
      email: email,
      password: password,
    })
    const jwtToken = response.data.token
    localStorage.setItem('jwtToken', jwtToken)
    return jwtToken
  } catch (error) {
    console.log('error getting jwt token', error)
    throw error
  }
}

export const CreateDefaultToken = async () => {
  const JWTData = await GetJWTToken('secret@gmail.com', 'Secret123@password')
  const token = JWTData.data.token
  return token
}
