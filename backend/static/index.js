$(document).ready(function () {

    namespace = '';

    var socket = io.connect(
        location.protocol + '//' + document.domain + ':' + location.port + namespace
    );

    // socket.on('connect', function() {
    //     socket.emit('my_event', {data: 'I\'m connected!'});
    // });

    socket.on('my_response', function (msg) {
        $('#log').append('<br>' + '<a>Server: </a>' + $('<div/>').text(msg).html());
    });

    socket.on('prediction', function (msg) {
        $('#log').append('<br>' + '<a>Prediction: </a>' + $('<div/>').text(msg).html());
    });

    var ping_pong_times = [];
    var start_time;
    window.setInterval(function () {
        start_time = (new Date).getTime();
        socket.emit('my_ping');
    }, 1000);

    // Handler for the "pong" message. When the pong is received, the
    // time from the ping is stored, and the average of the last 30
    // samples is average and displayed.
    socket.on('my_pong', function () {
        var latency = (new Date).getTime() - start_time;
        ping_pong_times.push(latency);
        ping_pong_times = ping_pong_times.slice(-30); // keep last 30 samples
        var sum = 0;
        for (var i = 0; i < ping_pong_times.length; i++)
            sum += ping_pong_times[i];
        $('#ping-pong').text(Math.round(10 * sum / ping_pong_times.length) / 10);
    });

    $('#echo').click(function (event) {
        socket.emit('echo', $('#emit_data').val());
        return false;
    });

    $("#emit_data").keyup(function (event) {
        if (event.keyCode === 13) {
            $("#echo").click();
        }
    });

    $('#broadcast').click(function (event) {
        socket.emit('broadcasting', $('#broadcast_data').val());
        return false;
    });

    $("#broadcast_data").keyup(function (event) {
        if (event.keyCode === 13) {
            $("#broadcast").click();
        }
    });

    $('#msg').click(function (event) {
        socket.emit('msg', $('#msg_data').val());
        return false;
    });

    $("#msg_data").keyup(function (event) {
        if (event.keyCode === 13) {
            $("#msg").click();
        }
    });

    $('#disconnect').click(function (event) {
        socket.emit('disconnect_request');
        return false;
    });
});