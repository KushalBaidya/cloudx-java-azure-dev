import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
    stages: [
        { duration: '1m', target: 100 },    // Ramp-up to 10 users over 1 minute
        { duration: '1m', target: 200},    // Spike to 50 users in 1 minute
        { duration: '10m', target: 200},    // Stay at 50 users for 5 minutes
    ],
};

export default function () {
    http.get('https://demo-petstorepetservice-cen-bhafdzawabdrckdp.centralindia-01.azurewebsites.net/petstorepetservice/v2/pet/info');
    sleep(1);
}
