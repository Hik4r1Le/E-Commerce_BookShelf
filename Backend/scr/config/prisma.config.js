import { PrismaClient } from '@prisma/client'
import fs from 'fs';
import path from 'path';

const caFilePath = path.join(process.cwd(), 'prisma', 'ca.pem');

function ensureCaCertificate() {
    try {
        if (!fs.existsSync(caFilePath)) {
            const base64Data = process.env.SSL_CERT_BASE64;
            
            if (!base64Data) {
                throw new Error('Missing environment variable SSL_CERT_BASE64 in file .env');
            }
            
            const decodedData = Buffer.from(base64Data, 'base64').toString('ascii');
            fs.writeFileSync(caFilePath, decodedData);
        }
    } catch (error) {
        console.error('❌ Error when processing CA certificate:', error.message);
    }
}

ensureCaCertificate();

const prisma = new PrismaClient();

async function testConnectionPrisma() {
  try {
    await prisma.$connect();
    console.log('✅ Prisma connected successfully!')
  } catch (error) {
    console.error('❌ Prisma connection failed:', error.message);
  } finally {
    await prisma.$disconnect();
  }
}

export { prisma, testConnectionPrisma};
