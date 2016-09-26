#include<stdio.h>
#include<winsock2.h>
#include<pthread.h>
#include<stdlib.h>
#include<math.h>
#pragma comment(lib,"wsock32.lib")

void send_message();
void error_handling(char * message);
void *clnt_connection(void * arg);
void *rasberryCommunicationThread(void *);
void *rasberryStarter(void *);
void clear();
float tempcal(int tempData);
//char state[10]="";
char *state;
pthread_mutex_t mutx;
int clnt_number = 0;
SOCKET clnt_socks[4];

float temperature1;
float temperature2;
char *p;
char aaaa[30];

void main() {
	SOCKET serverSocket, clientSocket;
	int clntAddrSize;
	struct sockaddr_in serverAddr;
	struct sockaddr_in clientAddr;
	int threadStatus;
	pthread_t threads;
	WSADATA wsaData;
	WSAStartup(WINSOCK_VERSION,&wsaData); // use TCP/IP socket funtion in winsock.dll
	
	//pthread_create(&threads, NULL, &rasberryCommunicationThread, 0);
	//pthread_join(threads, (void **)&threadStatus);
	pthread_create(&threads, NULL,&rasberryStarter, 0);
	if(pthread_mutex_init(&mutx,NULL))
		error_handling("mutex init error");
	memset(&serverAddr, 0, sizeof(serverAddr));

	serverSocket= socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);
	serverAddr.sin_family = AF_INET;
	serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
	serverAddr.sin_port = htons(5555);

	if(bind(serverSocket, (struct sockaddr*)&serverAddr, sizeof(serverAddr)) == -1)
		error_handling("bind() error");
	if(listen(serverSocket, 5) == -1)
		error_handling("listen() error");

		while(1){
			clntAddrSize = sizeof(clientAddr);
			clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clntAddrSize);
			pthread_mutex_lock(&mutx);
			pthread_create(&threads, NULL, clnt_connection, (void*) clientSocket);
			clnt_socks[clnt_number++] = clientSocket;
			pthread_mutex_unlock(&mutx);
		}
}

void *clnt_connection(void *arg){
	SOCKET clientSocket = (SOCKET) arg;
	int i;
	char and[40];
	//char *stringtemp1;
	//char *stringtemp2;
	char stringtemp1[10];
	char stringtemp2[10];
	itoa(temperature1, stringtemp1, 10);
	itoa(temperature2, stringtemp2, 10);

	sprintf(and, "%s%s%s%s%s%s", state,"#",stringtemp1, "#", stringtemp2,"#");
	send(clientSocket, and, sizeof(and), 0);
	//send_message();
	closesocket(clientSocket);
	printf("send to moblie  : %s\n",and);
	pthread_mutex_lock(&mutx);
	
	for(i=0; i<clnt_number; i++){
		if(clientSocket == clnt_socks[i])
		{
			for(;i<clnt_number-1;i++)
				clnt_socks[i] = clnt_socks[i+1];
			break;
		}
	}
	clnt_number--;
	pthread_mutex_unlock(&mutx);
	//clear();
	state = '\0';
	//memset(state, 0, sizeof(state));
	return 0;
}
/*
void send_message(){
	int i;
	pthread_mutex_lock(&mutx);

	for(i=0; i<clnt_number; i++)
		send(clnt_socks[i], state, sizeof(state),0);	
	clear();

	pthread_mutex_unlock(&mutx);
}
*/
void error_handling(char * message){
	fputs(message, stderr); // 에러메시지를stderr로스트림으로화면에출력
	fputc('\n', stderr);
	exit(1);
}

void *rasberryCommunicationThread(void *arg) {
	SOCKET serverSocket, clientSocket;
	struct sockaddr_in sin;
	struct sockaddr_in cli_addr;
	int size = sizeof(cli_addr);
	int i;
	float oldTemperature1 = 1023, oldTemperature2 = 1023;
	float gradient1, gradient2;
	//char tempData1[10]="";
	//char tempData2[10]="";
	//char flameData[10]="";
	char *tempData1;
	char *tempData2;
	char *flameData;
	char *temp_flame[3];
	serverSocket= socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);

	sin.sin_family = AF_INET;
	sin.sin_port = htons(5678); // 라즈베리포트번호5678
	sin.sin_addr.s_addr = htonl(ADDR_ANY);

	bind(serverSocket,(struct sockaddr*)&sin,sizeof(sin));
	listen(serverSocket,SOMAXCONN);
	//printf("Raspberry Connection Ready\n");
	clientSocket=accept(serverSocket,(struct sockaddr*)&cli_addr,&size);
	//printf("Raspberry Connected\n");
	while(1) {
		printf("asdfasdf");
		if( 0 == (recv(clientSocket, aaaa, sizeof(aaaa),0)))
			break;
		if(strlen(aaaa) > 14){
			memset(aaaa, 0, sizeof(aaaa));
			continue;
		}
			
		//printf("%s \n",aaaa);
		p = strtok(aaaa, "#");
		i = 0;
		while( p != NULL){
			temp_flame[i] = p;
			p = strtok(NULL, "#");
			i++;
		}
		tempData1 = temp_flame[0];
		tempData2 = temp_flame[1];
		flameData = temp_flame[2];
		/*
		p = strtok(aaaa,"#");
		tempData1 = p;
		p = strtok(NULL,"#");
		tempData2 = p;
		p = strtok(NULL,"#");
		flameData = p;
		p = strtok(NULL,"#");
		*/
		printf("tempData : %s\n",tempData1);
		printf("tempData : %s\n\n",tempData2);

		if (tempData1 != 0 && tempData2 != 0){
			temperature1 = tempcal(atoi(tempData1));
			temperature2 = tempcal(atoi(tempData2));
		}
		
		printf("temperature.... : %.2f\n",temperature1);
		printf("temperature.... : %.2f\n",temperature2);
		printf("flame       : %d\n\n",atoi(flameData));

		gradient1 = temperature1 - oldTemperature1;
		oldTemperature1 = temperature1;

		gradient2 = temperature2 - oldTemperature2;
		oldTemperature2 = temperature2;
		printf("asdfasdf");
		state = "fire";

		/*
		if(temperature1 > 35 && gradient1 >= 1 && atoi(flameData) > 100)
			state = "fire";
		else if(temperature2 > 35 && gradient2 >= 1 && atoi(flameData) > 100)
			state = "fire";
		else
			state = "normal";
		*/
		printf("asdfasdf");
		printf("State.......l... : %s\n",state);
	}
	closesocket(clientSocket);
	closesocket(serverSocket); 
}

void *rasberryStarter(void *arg){
	int threadStatus;
	pthread_t threads;
	while(1){
		pthread_create(&threads, NULL, &rasberryCommunicationThread, 0);
		pthread_join(threads, (void **)&threadStatus);
	}
}
void clear(){
	int i;
	for(i = 0; i < sizeof(state); i++){
		state[i]='\0';
	}
}

float tempcal(int tempData){
	float resistance;
	float temperature;
	resistance = 1023.0 / tempData -1;
	resistance = 10000 / resistance;
	temperature = resistance / 10000;
	temperature = log(temperature);
	temperature = temperature / 4200;
	temperature = temperature + 1.0 / (25 + 273.15);
	temperature = 1.0 / temperature;
	temperature -= 273.15;
	return temperature;
}