# Creating Key Pairs for Authentication

Since ssh-keygen does not support x509 format, thus use openssl to create public key.
The format of private(PKCS*#8) and public(X.509) keys are to be strictly followed due to library Key converter limitation.

create private key:
```shell
ssh-keygen -t rsa -b 4096 -m PKCS8 -f app.key
```
create public key:
```shell
openssl req -new -x509 -key app.key -out app.key.pub -days 365
```
