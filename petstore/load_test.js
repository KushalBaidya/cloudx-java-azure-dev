import http from 'k6/http';
import { sleep } from 'k6';

export default function () {
    http.get('https://demo-petstorepetservice-cen-bhafdzawabdrckdp.centralindia-01.azurewebsites.net/petstorepetservice/v2/pet/info');
    sleep(1); // Adjust the sleep time as needed
}
