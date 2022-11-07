Permission in Manifest :

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="com.android.vending.BILLING" />

Note : declare activity in manifest inside application tag

  
    <activity
        android:name=".PremiumActivity"
        android:exported="false"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>


dependency in build.gradle modelu level:{
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'


    implementation 'de.hdodenhof:circleimageview:3.0.0'

    implementation 'com.github.bumptech.glide:glide:4.9.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'

    def billing_version = "5.0.0"
    implementation "com.android.billingclient:billing:$billing_version"
    implementation 'com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava'
    implementation 'com.google.guava:guava:24.1-jre'


} 
// ///////////////////////////////////////////////////////////////////////////////not for you 

  val context = LocalContext.current
    Scaffold(
        topBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                /*horizontalAlignment = Alignment.CenterHorizontally*/
            ) {
                Row(
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.appbar_text), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }

                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                context.startActivity(Intent(context, PremiumActivity::class.java))
                            },
                        painter = painterResource(id = R.drawable.premium),
                        contentDescription = null,
                    )
                }

            }
            /*TopBar(title = stringResource(R.string.appbar_text))
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        context.startActivity(Intent(context, PremiumActivity::class.java))
                    },
                painter = painterResource(id = R.drawable.premium),
                contentDescription = null,
            )*/
        },
        modifier = Modifier
            .fillMaxSize(),
    )




////////////////////////////////////////////////////////////////////////////////////



@Composable
fun TopAppBar() {
    
    val context = LocalContext.current
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh Icon")
        }

        Text(text = "LiveScores", style = MaterialTheme.typography.h4)

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.modeicon),
                contentDescription = "Toggle Theme"
            )
        }

        IconButton(

            onClick = { openPaymtSreen(context)}) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.premium),
                contentDescription = "in app purchase Theme"
            )
        }
    }
}


   





