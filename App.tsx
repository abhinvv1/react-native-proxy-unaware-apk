import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet, ScrollView, NativeModules } from 'react-native';

const { ProxyUnawareModule } = NativeModules;

const App = () => {
  const [url, setUrl] = useState('');
  const [response, setResponse] = useState('');

  const sendRequest = async () => {
    try {
      const result = await ProxyUnawareModule.sendRequest(url);
      setResponse(result);
    } catch (error) {
      setResponse(`Error: ${error}`);
    }
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        onChangeText={setUrl}
        value={url}
        placeholder="Enter URL"
      />
      <Button title="Send Request on URL" onPress={sendRequest} />
      <ScrollView style={styles.responseContainer}>
        <Text style={styles.responseText}>{response}</Text>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 10,
    paddingHorizontal: 10,
  },
  responseContainer: {
    flex: 1,
    marginTop: 20,
    borderColor: 'gray',
    borderWidth: 1,
  },
  responseText: {
    padding: 10,
  },
});

export default App;
