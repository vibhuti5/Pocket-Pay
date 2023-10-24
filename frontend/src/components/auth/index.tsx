import { withAuthenticationRequired } from '@auth0/auth0-react'
import React from 'react'

interface AuthenticationGuardProps {
  component: React.ComponentType<any>
}

export const AuthenticationGuard: React.FC<AuthenticationGuardProps> = ({
  component: Component,
}) => {
  const AuthenticatedComponent = withAuthenticationRequired(Component)

  return <AuthenticatedComponent />
}
