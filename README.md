# edx

Tools & services for [Open] EDX learning platform.

Task number should be scpecified as an custom parameter `labId` in LTI module settings

### Отправка результатов в EDX
___
Для отправки результатов в EDX нам потребуется `sourcedId`, `outcomeServiceUrl` и `rating` - сам результат для отправки.
Кроме того, нам потребуюется `CONSUMER_KEY` и `CONSUMER_SECRET`, которые задаются в LTI паспорте в формате
```
[
    "lti_id:consumer_key:consumer_secret"
]
```
lti_id необходимо так же указать в настройках LTI блока

Всю чёрную магию по отправке данных в EDX за нас делает 
```xml
<dependency>
    <groupId>org.imsglobal</groupId>
    <artifactId>basiclti-util</artifactId>
    <version>1.2.0</version>
</dependency>
```
В своих кишках она использует следующий шаблон (он, и только он является истинно валидным):
```xml
<?xml version = "1.0" encoding = "UTF-8"?>
<imsx_POXEnvelopeRequest xmlns="http://www.imsglobal.org/services/ltiv1p1/xsd/imsoms_v1p0">
	<imsx_POXHeader>
		<imsx_POXRequestHeaderInfo>
			<imsx_version>"V1.0"</imsx_version>
			<imsx_messageIdentifier>"__СЛУЧАЙНОЕ_ЗНАЧЕНИЕ__"</imsx_messageIdentifier>
		</imsx_POXRequestHeaderInfo>
	</imsx_POXHeader>
	<imsx_POXBody>
		<replaceResultRequest>
			<resultRecord>
				<sourcedGUID>
					<sourcedId>"__sourcedId__"</sourcedId>
				</sourcedGUID>
				<result>
					<resultScore>
						<language>en</language>
						<textString>"__rating__"</textString>
					</resultScore>
				</result>
			</resultRecord>
		</replaceResultRequest>
	</imsx_POXBody>
</imsx_POXEnvelopeRequest>
```
Пример использования библиотеки:
```java
public int push(String sourcedId, String outcomeServiceUrl, float rating) throws IOException {
        HttpPost request = null;

        try {
            request = IMSPOXRequest.buildReplaceResult(c, CLIENT_KEY, CLIENT_SECRET, sourcedId, rating + "", null, true);
        } catch (OAuthException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse httpResponse = client.execute(request);
        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        log.debug("RESPONSE ENTITY:\n{}", responseString);
        return httpResponse.getStatusLine().getStatusCode();
    }
```

Если настройки LTI указаны верно, то всё должно пройти гладко, если же в ответе от EDX мы видим XML с ошибкой о проваленной валидации OAuth подписи, можно воспользоваться следующийм кодом:
```python
from oauthlib.oauth1.rfc5849 import signature
import mock
import urllib

def validate():
        client_key, client_secret = ('__CONSUMER_KEY__', u'__CONSUMER__SECRET__')
        headers = {
           # Exmple Authoriztion HEADER
           # OAuth oauth_body_hash="8Di4lHOBZn3ztioYj1VqCsE7jNs%3D", oauth_consumer_key="c28dcab7-b945-4f9e-88f9-5c54ae61416d.pglti.ifmo.ru", oauth_nonce="5089146837464679937", oauth_signature="DX9REUQvuAUbRWp0raTPdIYkZyo%3D", oauth_signature_method="HMAC-SHA1", oauth_timestamp="1553170389", oauth_version="1.0"
            'Authorization': unicode('''__AUTHORIZATION_HEADER__'''),
            'Content-Type': 'application/xml',
        }

        oauth_params = signature.collect_parameters(headers=headers, exclude_oauth_signature=False)
        oauth_headers = dict(oauth_params)
        oauth_signature = oauth_headers.pop('oauth_signature')
        print oauth_signature
        mock_request_lti_1 = mock.Mock(
            uri=unicode(urllib.unquote('__OUTCOME_URL__')),
            http_method=unicode('POST'),
            params=oauth_headers.items(),
            signature=oauth_signature
        )
        print 'Validation result: '+str(signature.verify_hmac_sha1(mock_request_lti_1, client_secret))

validate()
```
`АЛЯРМА!!!!: ` Для запуска этого кода необходимо использовать `ВТОРОЙ` питон, так же надо установить следующие зависимости:
```bash
    pip2 install oauthlib
    pip2 install mock
```

Сохраняем этот код в файл .py и запускаем с помощью python2