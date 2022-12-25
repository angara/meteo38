
// https://cloud.yandex.ru/docs/functions/quickstart/create-function/node-function-quickstart


exports.handler = async function (event, context) {
    name = event.queryStringParameters.name
    return {
        'statusCode': 200,
        'headers': {
            'Content-Type': 'text/plain'
        },
        'isBase64Encoded': false,
        'body': `Hello, ${name}!`
    }
};

// Добавьте файл hello.js в ZIP-архив hello-js.zip.
