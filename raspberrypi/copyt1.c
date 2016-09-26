#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdint.h>
#include <string.h>

#include <netinet/in.h>
#include <sys/socket.h>

#include <wiringPi.h>
#include <wiringPiSPI.h>

#include <pthread.h>

#define TRUE (1==1)
#define FALSE (!TRUE)

char stringData[10];
int tempData, tempData2, flameData;
char aaa[30];
//char *aaa;
char aa[10], bb[10], cc[10];
int myAnalogRead(int spiChannel,int channelConfig,int analogChannel){
    if(analogChannel<0 || analogChannel>7)
        return -1;
    unsigned char buffer[3] = {1};
    buffer[1] = (channelConfig+analogChannel) << 4;
    wiringPiSPIDataRW(spiChannel, buffer, 3);
    return ( (buffer[1] & 3 ) << 8 ) + buffer[2];
}

void *communication(){
	int c_socket;
	struct sockaddr_in s_addr;
	int len = 0;

	c_socket = socket(PF_INET, SOCK_STREAM, 0);
	memset(&s_addr, 0, sizeof(s_addr));

	s_addr.sin_addr.s_addr = inet_addr("192.168.0.4");
	s_addr.sin_family = AF_INET;
	s_addr.sin_port = htons(5678);

	if(-1 == connect(c_socket, (struct sockaddr*)&s_addr, sizeof(s_addr)))
		printf("fail\n");

	while(1) {
		sprintf(aa, "%d", tempData);
		sprintf(bb, "%d", tempData2);
		sprintf(cc, "%d", flameData);
		sprintf(aaa, "%s%s%s%s%s", aa, "#",bb, "#",cc);
		len = strlen(aaa);
		write(c_socket, aaa, len);
		printf("len : %d",len);
		printf("send to server : %s\n\n", aaa);
		arrClear();
/*
		sprintf(stringData, "%d", tempData2);
		len = strlen(stringData);
		write(c_socket, stringData, len);
		printf("send to server TempData2 : %s\n\n", stringData);
		arrClear();

		sprintf(stringData, "%d", flameData);
		len = strlen(stringData);
		write(c_socket, stringData, len);
		printf("send to server flameData : %s\n\n", stringData);
		arrClear();
*/
		sleep(1);
	}
		close(c_socket);
}
void *sensorRead1(){
	while(1){
		tempData = myAnalogRead(0, 8, 0);
		printf("TempData : %d\n", tempData);
		sleep(1);
	}
}
void *sensorRead2(){
	while(1){
		tempData2= myAnalogRead(0, 8, 1);
                printf("TempData2 : %d\n", tempData2);
		sleep(1);
	}
}
void *sensorRead3(){
	while(1){
		flameData= myAnalogRead(0, 8, 2);
		printf("FlameData : %d\n", flameData);
 		sleep(1);
	}
}

void arrClear(){
	int i;
	for(i = 0; i < sizeof(stringData); i++)
		stringData[i] = '\0';
}

int main (){
	printf("start");
	system("gpio load spi");
	wiringPiSetup();
	wiringPiSPISetup(0, 1000000);

	pthread_t p_thread[2];
	int threadStatus;

	pthread_create(&p_thread[0], NULL, &sensorRead1, 0);
	pthread_create(&p_thread[1], NULL, &communication, 0);
	pthread_create(&p_thread[2], NULL, &sensorRead2, 0);
	pthread_create(&p_thread[3], NULL, &sensorRead3, 0);

	pthread_join(p_thread[0], (void **)&threadStatus);
	pthread_join(p_thread[1], (void **)&threadStatus);
	pthread_join(p_thread[2], (void **)&threadStatus);
	pthread_join(p_thread[3], (void **)&threadStatus);

	return 0;
}
