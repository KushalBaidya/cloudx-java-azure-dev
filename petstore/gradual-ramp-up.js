import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 10 },   // Ramp-up to 10 users over 1 minute
        { duration: '5m', target: 10 },   // Stay at 10 users for 5 minutes
    ],
};

export default function () {
    http.get('https://demo-orderservice-centralindia.livelydune-9fa62620.centralindia.azurecontainerapps.io/petstoreorderservice/v2/store/info');
    sleep(1);
}
