import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.Secret
import java.nio.file.Files
import jenkins.model.Jenkins
import net.sf.json.JSONObject
import org.jenkinsci.plugins.plaincredentials.impl.*

// parameters
def jenkinsMasterKeyParameters = [
  description:  'Master Key for agent',
  id:           'agent_root',
  secret:       '',
  userName:     'root',
  key:          new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource('''-----BEGIN RSA PRIVATE KEY-----
MIIEpQIBAAKCAQEAxKcZBu/m8CW4r3mdQoJzYCdv5YoCx3t67b9y9XKI+7jvCEam
UJQ5RO03NzstuOf1JLesZg1qx3TFHe621xxQU7/ScSy5Ns9kg7Q1alj4v/MMyo1O
4Cj3ortBRb81Cnfeymf6tqYWDl8IaEITDnCvxSWAqOPv4w+aw+VI/C8+aeQFDrmB
LMBZCf0/RpUHub/i3YTQjBsAYk7PT6vL0yVxIpR+15Q1kgs4OhZm4Dbc8ZSGrCLV
7Z5q1OUVDRMtJMZwveThBvQpqadwN5/dCtn68oYBR5m9ZyzmCEiYZvnGiKdxJEY5
bXUAvp8j729AIiFJrI0SgcbWm1Qb5xLtTm5EOwIDAQABAoIBACjLLQLmYzav7Ibi
KIAyhXKN28V3AqqU7+Q/0b4e21G0hEloNnoCtXqZ5tlbOlngLFdIjsfAwsTLcVSL
P3ySEAu/gA0gJO+8x/IWQPplqHxRE/c0pwcTGIO2aozoZD1ermW4eibHf03N4vWV
t1RIhf3UkML4PbwRcZ5vgttlhYH7gHf+oOZkP7pA4/VJUcEFsLF1ogSuvI/bcY3g
tIU5tN+FR05+PbNuO1DFPPrq3TwaoTWh+Xe3uxgiPTApUbqH/AxwbaXKzkioDpKo
NGb+qealzShj06ouxRkPOATRF+zO+wvLZ1jbJw5FfgXao4cCtSzxfEdkNCT5mLVc
xs5lprkCgYEA+kOvF8kyAibuTiKhVvqjZ7MTlLqoULRJ7eE7vF8qj+fKR17GQ5gy
eEL7VthiUVl1yB2Pxl23w3hBRs1d3QIWowKUmxOSu1Xgry+wH7hRiaD04nFLbaVJ
fHajfzAn6TGSA+1Yp3sHEHVhJTukqHCyTolj0zcLOJmjqn0fjRloiP0CgYEAySje
+MhLzyXIJ4Aoz3PbPSxRuKh6QGFvfalfRmH+HeWnkmiP86OkYxy8w7Bict7UvBqh
w/axVFdJJllYhMT5FLIYs/Q8/hueGEJQqKSOo8wVWf+yDjCHI0wBAio9Klb49Sy2
o5YTVAES1HpYlfehB2I7DoEpoiot+QgUZrgcg5cCgYEAzMLWHpsCLJqXvhw9b4Hw
cmclbTnvfuCT4Gq5Sl1+4HEgopZbl3COUrFTHXI7tx1/7bSqLv383ZSlq/1Zf/s2
dDcnSV6RXm9cP1nAO6O+KuIjmqZhtZtX7drP2L+Y2AYz1ry6HYXeeMvhKOqMxTg3
22Qz5duSdcPXEiX/y51vVy0CgYEAgO1lG2akz/uTMV3+yhejaK3DIus4j5LjdlLb
8axUxqyunjPOL4WukqeUhIfEUYNoLbeT/plaRog7Jg0VouLsEcuj8ZKUlPj2du3p
7Gyrz2e6N5iecmG/+2t3KgAQe9aKcflahbZmZOzbOLvBkzzHRIg8+arnGDyYgogf
K0bNX7kCgYEAs78/fTmCLmd1iw2efXsO3DBmA7UEU/Y9moUTspAz1ZeKHvo6cs59
7nKpeoDTAwKztSSeKry8rmM22zrgsP+yJ2etqynttKqS+4Hs73q50OqkocBh4ZU2
5GpGaUse4WX0R+iZiqfWmfDKqFHzHz5F9RYozwIqNEAXyXHHsEuQKIA=
-----END RSA PRIVATE KEY-----''')
]

// get Jenkins instance
Jenkins instance = Jenkins.getInstance()

// get credentials domain
def domain = Domain.global()

// get credentials store
def store = instance.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// define private key
def privateKey = new BasicSSHUserPrivateKey(
  CredentialsScope.GLOBAL,
  jenkinsMasterKeyParameters.id,
  jenkinsMasterKeyParameters.userName,
  jenkinsMasterKeyParameters.key,
  jenkinsMasterKeyParameters.secret,
  jenkinsMasterKeyParameters.description
)

// add credential to store
store.addCredentials(domain, privateKey)

// save to disk
instance.save()
println("Credentials have been added successfully.")