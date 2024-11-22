const NodeMediaServer = require('node-media-server');
const cp = require('child_process');
// var axios=require("axios")
const data = [
    // {
    //     "cid": 3,
    //     "ip": "192.168.1.101",
    //     "port": "8980",
    // },
    // {
    //     "cid": 4,
    //     "ip": "192.168.1.101",
    //     "port": "8981",
    // },
    {
        "cid": 1,
        "ip": "192.168.108.187",
        "port": "8979",
    },
    // {
    //     "cid": 5,
    //     "ip": "192.168.1.101",
    //     "port": "8982",
    // },
    // {
    //     "cid": 6,
    //     "ip": "192.168.1.101",
    //     "port": "8083",
    // }
];
//执行cmd
function runCmdTest(cmd) {
    cp.exec(`${cmd}`, (err, stdout, stderr) => {
    });
}
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
const config = {
    rtsporvideo: {
        src: 'fall3.mp4'
    },
    rtmp: {
        port: 1935,
        chunk_size: 60000,
        gop_cache: true,
        ping: 60,
        ping_timeout: 30
    },
    http: {
        port: 8979,
        mediaroot: './media/', // 建议写
        allow_origin: '*',
    },
    trans: { // 这里参数是trans参数，不是relay参数，relay参数中配置hls无效
        ffmpeg: 'ffmpeg.exe',//指明FFmpeg位置
        tasks: [
            {
                app: 'live',
                // ac: 'acc',
                // vc: 'libx264',
                hls: true,
                hlsFlags: '[hls_time=1:hls_list_size=5:hls_flags=delete_segments]',
                dash: true,
                dashFlags: '[f=dash:window_size=3:extra_window_size=5]'
            }
        ]
    }
};
async function slp(config) {
    var nms = new NodeMediaServer(config);
    nms.run();
    await sleep(1000);
    runCmdTest('ffmpeg -i ' + config.rtsporvideo.src + ' -c copy -f  flv rtmp://localhost:' + config.rtmp.port + '/live/STREAM_NAME');
    // runCmdTest('ffmpeg -re -stream_loop -1 -i '+config.rtsporvideo.src+' -c copy -f  flv rtmp://localhost:'+config.rtmp.port+'/live/STREAM_NAME')
}
data.forEach(element => {
    config.rtsporvideo.src = "rtsp://admin:latex123@" + element.ip + ":554/Streaming/Channels/101";
    config.http.port = element.port;
    config.rtmp.port = 1900 + element.cid;
    config.http.mediaroot = './media/' + element.cid + '/';
    slp(JSON.parse(JSON.stringify(config)));
});


// slp(config)

// const GetCam = (uid, callback) => {
//     axios({
//         method: "post",
//         url: "http://localhost:8000/GetCam",
//         data: {
//             "uid":uid
//         }
//      }).then((res) => {
//           callback && callback(res.data);
//      });
//  }
// GetCam("1",function(res){
//     res.data.forEach(element => {
//         // console.log(element)
//         config.http.port=element.port
//         config.rtmp.port=1900+element.cid
//         config.http.mediaroot='./media/'+element.cid+'/'
//         slp(JSON.parse(JSON.stringify(config)))
//     });
// });


