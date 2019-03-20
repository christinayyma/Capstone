from flask import Flask, render_template, session, request
from flask_socketio import SocketIO, send, emit, disconnect

async_mode = "eventlet"

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app, async_mode=async_mode)

 
@app.route('/')
def index():
    return render_template('index.html')

@socketio.on('echo')
def test_message(message):
    print('echo: ', message)
    emit('my_response', message)

@socketio.on('broadcasting')
def test_message(message):
    print('broadcasting: ', message)
    emit('my_response', message, broadcast=True)

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
    emit('my_response',
         {'data': 'Disconnected!', 'count': session['receive_count']})
    disconnect()

@socketio.on('disconnect')
def test_disconnect():
    print('Client disconnected')



if __name__ == '__main__':
    socketio.run(app, debug=True)