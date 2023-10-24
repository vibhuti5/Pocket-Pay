import axios from 'axios'
export const BASE_URL = 'https://be-bc-105.bootcamp64.tk'

export const API = axios.create({
  baseURL: BASE_URL,
})

API.interceptors.request.use(
  (config) => {
    const jwtToken = localStorage.getItem('token')
    if (shouldIncludeToken(config.url)) {
      config.headers.Authorization = `Bearer ${jwtToken}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

function shouldIncludeToken(url: any) {
  const protectedEndpoints = [
    '/businesses',
    '/business-types',
    '/personal-details',
    '/owners',
    '/transactions',
    '/recipients',
    '/payment-methods',
    '/bank-accounts',
    '/debit-cards',
  ]
  return protectedEndpoints.some((endpoint) => url.includes(endpoint))
}
