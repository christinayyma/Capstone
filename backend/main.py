from flask import Flask, render_template, session, request
from flask_socketio import SocketIO, send, emit, disconnect
from predict import predict
from threading import Thread


async_mode = "eventlet"

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, async_mode=async_mode)

result = ""
prevResult = ""
signalCount = 0

 
@app.route('/')
def index():
    # global thread
    # if thread is None:
    #     thread = Thread(target=setInterval(foo,1))
    #     thread.start()
    return render_template('index.html')

@socketio.on('echo')
def echo(message):
    print('echo: ', message)
    emit('my_response', message)

@socketio.on('broadcasting')
def broadcast(message):
    print('broadcasting: ', message)
    emit('my_response', message, broadcast=True)

@socketio.on('msg')
def msg(message):
    if (message == ''):
        message = '-5488 11796 5320 4151 3606 -8208 535 777 768 745 867'
    print('msg:', message)

    global result
    global prevResult
    result = predict(message)
    countSignal();
    print('count:', signalCount)
    if (signalCount == 7):
        emit('prediction', result)
    else:
        emit('prediction', "0")
    prevResult = result

@socketio.on('connect')
def test_connect():
    print('a client connected!')
    emit('my_response', 'Connected!')

@socketio.on('my_ping')
def ping_pong():
    emit('my_pong')

@socketio.on('disconnect_request')
def disconnect_request():
    session['receive_count'] = session.get('receive_count', 0) + 1
    emit('my_response', 'Disconnected!')
    disconnect()

@socketio.on('disconnect')
def test_disconnect():
    print('Client disconnected')


# def setInterval(func,time):
#     e = threading.Event()
#     while not e.wait(time):
#         func()

# def foo():
#     print("hello")

# count the numnber of consistent signals
# need at least 7 consistent signals to return a result
def countSignal():
    global signalCount
    if (result == prevResult):
        print('incremeneting')
        signalCount = signalCount + 1
    else:
        signalCount = 0


if __name__ == '__main__':
    socketio.run(app, host='0.0.0.0', debug=True)

