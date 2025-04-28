#!/bin/bash

#=========================
# Solution Manage Console
#=========================

# NGINX ENV
export NGINX_GREP=nginx: master
export NGINX_PORT=443
export NGINX_URL=/health

# TOMCAT8 ENV
export TOMCAT8_PATH=/app/tomcat8.5.99
export TOMCAT8_GREP=app/tomcat8.5.99
export TOMCAT8_PORT=8080
export TOMCAT8_URL=

# TOMCAT9 ENV
export TOMCAT9_PATH=/app/tomcat9.0.104
export TOMCAT9_GREP=app/tomcat9.0.104
export TOMCAT9_PORT=8080
export TOMCAT9_URL=

# TOMCAT10 ENV
export TOMCAT10_PATH=/app/tomcat10.1.9
export TOMCAT10_GREP=app/tomcat10.1.9
export TOMCAT10_PORT=8080
export TOMCAT10_URL=

# ORACLE12 ENV
export ORACLE12_CONTAINER=oracle12c
export ORACLE12_PORT=1521

# ORACLE19 ENV
export ORACLE19_CONTAINER=oracle19c
export ORACLE19_PORT=1521

# MYSQL ENV
export MYSQL_CONTAINER=mysql
export MYSQL_PORT=3306


# FUNCTION ZONE --------------------------------------------------------------------------

check_service_process() { # Check service only process
	# If process is alive, echo "On" with green color
	if ps -aux | grep -E "$1" | grep -v grep > /dev/null; then
		echo -e "\e[32mOn\e[0m"
		
	# If process is dead, echo "Off" with red color
	else
		echo -e "\e[31mOff\e[0m"
	fi
}

check_container() { # Check docker container
	# If container is running, echo "On" with green color
	if [ "$(docker ps -q -f name=$1)" ]; then
		echo -e "\e[32mOn\e[0m"
		
	# If process is exited, echo "Off" with red color
	else
		echo -e "\e[31mOff\e[0m"
	fi
}

stop_service() { # Stop service
	# Common before work step
	# 1. Disable input 2. Launch animation 3. Return PID of animation
	stty -echo -icanon
	loading_animation &
	animation_pid=$!
	
	# 1. Stop service 2. Wait until process shutdown
	# Threshold : You need DOCKER_COMPOSE to check detail health about docker container..
	if [ "$1" = "NGINX" ]; then
		nginx -s stop > /dev/null 2>&1
		wait_shutdown_process "$NGINX_GREP"
	elif [ "$1" = "TOMCAT8" ]; then
		cd $TOMCAT8_PATH/bin && ./shutdown.sh > /dev/null 2>&1
		wait_shutdown_process "$TOMCAT8_GREP"
	elif [ "$1" = "TOMCAT9" ]; then
		cd $TOMCAT9_PATH/bin && ./shutdown.sh > /dev/null 2>&1
		wait_shutdown_process "$TOMCAT9_GREP"
	elif [ "$1" = "TOMCAT10" ]; then
		cd $TOMCAT10_PATH/bin && ./shutdown.sh > /dev/null 2>&1
		wait_shutdown_process "$TOMCAT10_GREP"
	elif [ "$1" = "ORACLE12" ]; then
		docker stop "$ORACLE12_CONTAINER" > /dev/null 2>&1
	elif [ "$1" = "ORACLE19" ]; then
		docker stop "$ORACLE19_CONTAINER" > /dev/null 2>&1
	elif [ "$1" = "MYSQL" ]; then
		docker stop "$MYSQL_CONTAINER" > /dev/null 2>&1
	else
		echo "Unknown Error"
	fi
	
	# Common after work step
	# 1. Kill process of animation 2. Truncate buffer 3. Enable input
	kill $animation_pid 2>/dev/null
	wait $animation_pid 2>/dev/null
	while read -t 0.1 -n 10000; do : ; done
	stty echo icanon
}

start_service() { # Start service
	# Common before work step
	# 1. Disable input 2. Launch animation 3. Return PID of animation
	stty -echo -icanon
	loading_animation &
	animation_pid=$!
	
	# 1. Start service 2. Wait until service startup
	# Threshold : You need DOCKER-COMPOSE to check detail health about docker container..
	if [ "$1" = "NGINX" ]; then
		nginx > /dev/null 2>&1
		wait_service_curl "$NGINX_GREP" "$NGINX_PORT" "$NGINX_URL" "https"
	elif [ "$1" = "TOMCAT8" ]; then
		cd $TOMCAT8_PATH/bin && ./startup.sh > /dev/null 2>&1
		wait_service_curl "$TOMCAT8_GREP" "$TOMCAT8_PORT" "$TOMCAT8_URL" "http"
	elif [ "$1" = "TOMCAT9" ]; then
		cd $TOMCAT9_PATH/bin && ./startup.sh > /dev/null 2>&1
		wait_service_curl "$TOMCAT9_GREP" "$TOMCAT9_PORT" "$TOMCAT9_URL" "http"
	elif [ "$1" = "TOMCAT10" ]; then
		cd $TOMCAT10_PATH/bin && ./startup.sh > /dev/null 2>&1
		wait_service_curl "$TOMCAT10_GREP" "$TOMCAT10_PORT" "$TOMCAT10_URL" "http"
	elif [ "$1" = "ORACLE12" ]; then
		docker start "$ORACLE12_CONTAINER" > /dev/null 2>&1
	elif [ "$1" = "ORACLE19" ]; then
		docker start "$ORACLE19_CONTAINER" > /dev/null 2>&1
	elif [ "$1" = "MYSQL" ]; then
		docker start "$MYSQL_CONTAINER" > /dev/null 2>&1
	else
		echo "Unknown Error"
	fi
	
	# Common after work step
	# 1. Kill process of animation 2. Truncate buffer 3. Enable input
	kill $animation_pid 2>/dev/null
	wait $animation_pid 2>/dev/null
	while read -t 0.1 -n 10000; do : ; done
	stty echo icanon
}

wait_shutdown_process() { # Waiting shutdown service with process
	while true; do
		# Grep process
		if ps -aux | grep -E "$1" | grep -v grep > /dev/null; then
			sleep 1
		else
			return
		fi
	done
}

wait_service_curl() { # Waiting service with curl
	if ps -aux | grep -E "$1" | grep -v grep > /dev/null; then
		while true; do
			# Check http status by curl
			if [ "$4" = "https" ]; then
				http_status=$(curl -s -o /dev/null -w "%{http_code}" -L -k --connect-timeout 5 --max-time 5 $4://localhost:$2/$3)
			else
				http_status=$(curl -s -o /dev/null -w "%{http_code}" -L --connect-timeout 5 --max-time 5 $4://localhost:$2/$3)
			fi
			
			if [ "$http_status" = "200" ]; then
				return
			else
				sleep 1
			fi
		done
	fi
}

wait_service_process() { # Wating service only process
	while true; do
		# Grep process
		if ps -aux | grep -E "$1" | grep -v grep > /dev/null; then
			return
		else
			sleep 1
		fi
	done
}

switch_jdk_version() { # Switch JDK version
	echo "현재 Java 버전:"
	java -version 2>&1 | head -n 1
	echo ""

	echo "설치된 Java 목록:"
	JAVA_PATHS=($(alternatives --display java | grep -E '^/usr' | awk '{print $1}'))

	i=1
	for path in "${JAVA_PATHS[@]}"; do
		echo "$i. $path"
		((i++))
	done

	echo ""
	read -p "변경할 Java 번호: " num

	SELECTED_PATH="${JAVA_PATHS[$((num-1))]}"
	SELECTED_DIR=$(dirname "$SELECTED_PATH")

	# 1. Find default bin dir
	# 2. If in jre/bin/java, find upper bin dir
	JAVAC_PATH="${SELECTED_DIR}/javac"
	
	if [ ! -f "$JAVAC_PATH" ]; then
		PARENT_DIR=$(dirname "$SELECTED_DIR")
		GRAND_PARENT_DIR=$(dirname "$PARENT_DIR") 
		JAVAC_PATH="${GRAND_PARENT_DIR}/bin/javac"
	fi

	if [ ! -f "$JAVAC_PATH" ]; then
		echo "잘못된 입력 혹은 경로 오류"
		read -n 1 -s -r -p "계속하려면 아무 키 입력"
		return
	fi
	
	stty -echo -icanon
	loading_animation &
	animation_pid=$!
	
	alternatives --set java "$SELECTED_PATH"
	alternatives --set javac "$JAVAC_PATH"
	
	kill $animation_pid 2>/dev/null
	wait $animation_pid 2>/dev/null
	while read -t 0.1 -n 10000; do : ; done
	stty echo icanon
}

loading_animation() { # loading animation
	frames=('-' '\' '|' '/')
	
	while true; do
		for frame in "${frames[@]}"; do
			# Loading frame change with yellow color in 0.2 seconds
			echo -ne "\r\e[33mLoading... $frame \033[K\e[0m"
			sleep 0.2
		done
	done
}
# FUNCTION END ---------------------------------------------------------------------------

# Show console main menu
while true; do
	# Clear console everytime
	clear
	
	# TO-DO : MAKE MENU FUNCTION
	echo "=========================="
	echo "      Manage Console"
	echo "--------------------------"
	echo "00. Refresh"
	echo "11. JDK - $(java -version 2>&1 | head -n 1 | grep -oP '"\K[0-9._]+')"
	echo "21. Nginx - $(check_service_process $NGINX_GREP)"
	echo "    WAS"
	echo "31. └ Tomcat8 - $(check_service_process $TOMCAT8_GREP)"
	echo "32. └ Tomcat9 - $(check_service_process $TOMCAT9_GREP)"
	echo "33. └ Tomcat10 - $(check_service_process $TOMCAT10_GREP)"
	echo "    Docker"
	echo "41. └ Oracle12c - $(check_container $ORACLE12_CONTAINER)"
	echo "42. └ Oracle19c - $(check_container $ORACLE19_CONTAINER)"
	echo "43. └ MySQL - $(check_container $MYSQL_CONTAINER)"
	echo "Else. Exit"
	echo "=========================="
	echo -n ">> "
	
	read choice
	
	# TO-DO : MAKE CHOICE FUNCTION
	case $choice in
		00)
			pj && exec "$0"
			;;
		11)
			switch_jdk_version
			;;
		21)
			if ps -aux | grep -E "$NGINX_GREP" | grep -v grep > /dev/null; then
				stop_service "NGINX"
			else
				start_service "NGINX"
			fi
			;;
		31)
			if ps -aux | grep -E "$TOMCAT8_GREP" | grep -v grep > /dev/null; then
				stop_service "TOMCAT8"
			else
				start_service "TOMCAT8"
			fi
			;;
		32)
			if ps -aux | grep -E "$TOMCAT9_GREP" | grep -v grep > /dev/null; then
				stop_service "TOMCAT9"
			else
				start_service "TOMCAT9"
			fi
			;;
		33)
			if ps -aux | grep -E "$TOMCAT10_GREP" | grep -v grep > /dev/null; then
				stop_service "TOMCAT10"
			else
				start_service "TOMCAT10"
			fi
			;;
		41)
			if [ "$(docker ps -q -f name=$ORACLE12_CONTAINER)" ]; then
				stop_service "ORACLE12"
			else
				start_service "ORACLE12"
			fi
			;;
		42)
			if [ "$(docker ps -q -f name=$ORACLE19_CONTAINER)" ]; then
				stop_service "ORACLE19"
			else
				start_service "ORACLE19"
			fi
			;;
		43)
			if [ "$(docker ps -q -f name=$MYSQL_CONTAINER)" ]; then
				stop_service "MYSQL"
			else
				start_service "MYSQL"
			fi
			;;
		*)
			exit 0
			;;
	esac
done
