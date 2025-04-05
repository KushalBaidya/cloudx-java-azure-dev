import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
    http.get('https://demo-orderservice-centralindia.livelydune-9fa62620.centralindia.azurecontainerapps.io/petstoreorderservice/v2/store/info');
    sleep(1); // Adjust the sleep time as needed
}
